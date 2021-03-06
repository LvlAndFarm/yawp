package io.yawp.repository.actions.parents;

import io.yawp.commons.http.annotation.PUT;
import io.yawp.repository.IdRef;
import io.yawp.repository.actions.Action;
import io.yawp.repository.models.parents.Child;
import io.yawp.repository.models.parents.Grandchild;

import java.util.List;

public class GrandchildAction extends Action<Grandchild> {

    @PUT("touched")
    public Grandchild touchObject(IdRef<Grandchild> id) {
        Grandchild grandchild = id.fetch();
        grandchild.setName("touched " + grandchild.getName());
        return grandchild;
    }

    @PUT("touched")
    public List<Grandchild> touchCollection(IdRef<Child> childId) {
        List<Grandchild> grandchilds = yawp(Grandchild.class).from(childId).list();
        for (Grandchild grandchild : grandchilds) {
            grandchild.setName("touched " + grandchild.getName());
        }
        return grandchilds;
    }

}
