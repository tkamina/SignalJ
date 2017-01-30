/* Copyright (c) 2016-2017, Tetsuo Kamina
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package signalj;

import org.extendj.JavaCompiler;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.Problem;
import java.util.Collection;
import java.util.LinkedList;

public class SignalJCompiler extends JavaCompiler {

    public static void main(String args[]) {
	int exitCode = new SignalJCompiler().run(args);
	if (exitCode != 0) {
	    System.exit(exitCode);
	}
    }

    public SignalJCompiler() {
	super("SignalJ powered by ExtendJ");
    }

    protected SignalJCompiler(String toolName) {
	super(toolName);
    }

    protected int processCompilationUnit(CompilationUnit unit) {
	Collection<Problem> result = unit.checkSignalTypes();
	if (!result.isEmpty()) {
	    for (Problem p: result) {
		System.err.println(p);
	    }
	    return EXIT_ERROR;
	}
	unit.rewriteSignalTypes();
	return super.processCompilationUnit(unit);
    }
}
