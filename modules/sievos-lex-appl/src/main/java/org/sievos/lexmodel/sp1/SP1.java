/*
 * Copyright 2016-2018 wapitia.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of wapitia.com or the names of contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * WAPITIA.COM ("WAPITIA") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL WAPITIA OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * WAPITIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package org.sievos.lexmodel.sp1;

import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdCompiler;
import org.sievos.lexmodel.std.StdExecutable;
import org.sievos.lexmodel.std.StdPart;
import org.sievos.lexmodel.std.StdPartFunction;

/**
 * In the SP1 system, all static results are some type of partition,
 * returned as a {@link PartLN}
 */
public interface SP1 {

	public static final Executable FNULL = null;

	public static final PartLN NULLPART = null;


	public boolean isNullResult(PartLN part);

	interface Compiler extends StdCompiler<PartLN> {

		@Override
		Executable compile(String expression);
	}


	interface Executable extends StdExecutable<PartLN> {

		/**
		 * Execute this function, returning its result as some Sievos SP1
		 * result derivative {@code R}
		 *
		 * @see org.sievos.lexmodel.SievosExecutable#execute()
		 * @see SP1#Result
		 */
		@Override
		public StdPart execute();
	}

	interface PartFunction extends StdPartFunction {

		@Override
		StdBund execute(StdBund bund);
	}
}
