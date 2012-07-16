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

import org.jboss.weld.environment.se.StartMain;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import br.gov.frameworkdemoiselle.internal.bootstrap.ShutdownBootstrap;
import br.gov.frameworkdemoiselle.internal.bootstrap.StartupBootstrap;
import br.gov.frameworkdemoiselle.util.Beans;

public class DemoiselleRunner extends BlockJUnit4ClassRunner {

	private static boolean containerInitialized = false;

	public DemoiselleRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		init();
	}

	private synchronized void init() {
		if (!containerInitialized) {
			StartMain.main(null);
			containerInitialized = true;
		}
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
	}

	protected Object createTest() throws Exception {
		return Beans.getReference(getTestClass().getJavaClass());
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		StartupBootstrap.startup();
		return super.withBeforeClasses(statement);
	}

	@Override
	protected Statement withAfterClasses(Statement statement) {
		Statement result = super.withAfterClasses(statement);
		ShutdownBootstrap.shutdown();

		return result;
	}
}
