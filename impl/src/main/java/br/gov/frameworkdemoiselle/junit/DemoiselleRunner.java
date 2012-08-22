/*
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 * 
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 * 
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 * 
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 * 
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package br.gov.frameworkdemoiselle.junit;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import br.gov.frameworkdemoiselle.internal.bootstrap.BeforeApplicationFinalization;
import br.gov.frameworkdemoiselle.internal.bootstrap.BeforeApplicationInitialization;

public class DemoiselleRunner extends BlockJUnit4ClassRunner {

	private Weld weld;

	private WeldContainer container;

	private ClassLoader classLoader;

	// private static boolean containerInitialized = false;

	// private static Integer count = 0;

	private int count;

	public DemoiselleRunner(Class<?> testClass) throws InitializationError {
		super(getFromTestClassloader(testClass));

		this.classLoader = getTestClass().getJavaClass().getClassLoader();

		init();
	}

	private static Class<?> getFromTestClassloader(Class<?> clazz) throws InitializationError {
		try {
			ClassLoader c = new TestClassLoader();
			// ClassLoader classLoader = new URLClassLoader();
			return Class.forName(clazz.getName(), true, c);
		} catch (ClassNotFoundException e) {
			throw new InitializationError(e);
		}
	}

	@SuppressWarnings("unchecked")
	private/* synchronized */void init() {
		// if (!containerInitialized) {

		// this.classLoader = new TestClassLoader();
		Class<Weld> type;

		try {
			type = (Class<Weld>) Class.forName(Weld.class.getName(), true, this.classLoader);
			this.weld = type.newInstance();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// this.weld = new Weld();
		this.container = weld.initialize();
		this.count = this.testCount();

		// Beans.setBeanManager(container.getBeanManager());

		// StartMain.main(null);
		// containerInitialized = true;

		startup();
		// }

		// count += testCount();
	}

	// @Override
	// protected void finalize() throws Throwable {
	// shutdown();
	// super.finalize();
	// }

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		// Beans.setBeanManager(container.getBeanManager());
		super.runChild(method, notifier);

		// count--;
		// count--;

		if (--count == 0) {
			// if (count == 0) {
			shutdown();
			// container.f
			// } else {
			// while (count != 0) {
			// try {
			// Thread.currentThread().sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// }
		}
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
		weld.shutdown();
		// container.instance().s
	}

	// @SuppressWarnings("unused")
	protected Object createTest() throws Exception {
		// Beans.setBeanManager(container.getBeanManager());
		return container.instance().select(getTestClass().getJavaClass()).get();
		//
		// return Beans.getReference(getTestClass().getJavaClass());
	}

	private void startup() {
		// Beans.setBeanManager(container.getBeanManager());
		container.event().select(BeforeApplicationInitialization.class).fire(new BeforeApplicationInitialization() {

			@Override
			public boolean removeProcessors() {
				return true;
			}
		});

		// Beans.getBeanManager().fireEvent(new BeforeApplicationInitialization() {
		//
		// @Override
		// public boolean removeProcessors() {
		// return false;
		// }
		// });
	}

	private void shutdown() {
		// Beans.setBeanManager(container.getBeanManager());
		container.event().select(BeforeApplicationFinalization.class).fire(new BeforeApplicationFinalization() {

			@Override
			public boolean removeProcessors() {
				return true;
			}
		});

		// Beans.getBeanManager().fireEvent(new BeforeApplicationFinalization() {
		//
		// @Override
		// public boolean removeProcessors() {
		// return false;
		// }
		// });
	}

}
