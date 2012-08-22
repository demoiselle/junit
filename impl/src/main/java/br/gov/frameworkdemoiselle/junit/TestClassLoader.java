package br.gov.frameworkdemoiselle.junit;

import java.net.URLClassLoader;

public class TestClassLoader extends URLClassLoader {

	public TestClassLoader() {
		super(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
//		if (name.startsWith("java.")) {
//			return super.findClass(name);
//		}
		return super.loadClass(name);
	}
}
