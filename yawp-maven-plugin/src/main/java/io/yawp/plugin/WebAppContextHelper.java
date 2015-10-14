package io.yawp.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.util.Scanner;
import org.mortbay.util.Scanner.DiscreteListener;

public class WebAppContextHelper {

	protected DevServerMojo mojo;

	protected WebAppContext webapp;

	public WebAppContextHelper(DevServerMojo mojo) {
		this.mojo = mojo;
	}

	public WebAppContext createWebApp() {
		webapp = new WebAppContext(mojo.getAppDir(), "");
		webapp.setDefaultsDescriptor(getWebDefaultXml());
		configureClassloader();
		configureHotDeploy();
		return webapp;
	}

	protected String getWebDefaultXml() {
		return "/webdefault.xml";
	}

	private void configureClassloader() {
		webapp.setClassLoader(createClassLoader(getCustomClassPathElements()));
	}

	protected List<String> getCustomClassPathElements() {
		return new ArrayList<String>();
	}

	protected void configureHotDeploy() {
		getLog().info("HotDeploy scanner: " + mojo.getHotDeployDir());
		Scanner scanner = new Scanner();
		scanner.setScanInterval(mojo.getFullScanSeconds());
		scanner.setScanDirs(Arrays.asList(new File(mojo.getHotDeployDir())));
		scanner.addListener(new DiscreteListener() {

			@Override
			public void fileChanged(String filename) throws Exception {
				fileAdded(filename);
			}

			@Override
			public void fileAdded(String filename) throws Exception {
				if (!webapp.isStarted()) {
					return;
				}
				getLog().info(filename + " updated, reloading the webapp!");
				restart(webapp);
			}

			@Override
			public void fileRemoved(String filename) throws Exception {
			}
		});
		scanner.scan();
		scanner.start();
	}

	private Log getLog() {
		return mojo.getLog();
	}

	private void restart(WebAppContext webapp) throws Exception {
		webapp.stop();
		configureClassloader();
		webapp.start();
	}

	protected URLClassLoader createClassLoader(List<String> customClasspathElements) {
		try {
			List<URL> urls = new ArrayList<URL>();

			addURLs(mojo.getProject().getRuntimeClasspathElements(), urls);
			addURLs(customClasspathElements, urls);

			return new URLClassLoader(urls.toArray(new URL[] {}), Thread.currentThread().getContextClassLoader());
		} catch (DependencyResolutionRequiredException | MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected void addURLs(List<String> classpathElements, List<URL> runtimeUrls) throws MalformedURLException {
		List<String> elements = classpathElements;
		for (int i = 0; i < elements.size(); i++) {
			String element = (String) elements.get(i);
			runtimeUrls.add(new File(element).toURI().toURL());
			getLog().debug("Adding to webapp classpath: " + element);
		}
	}

}