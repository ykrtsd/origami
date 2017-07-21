package blue.origami.transpiler.code;

import blue.origami.transpiler.TCodeSection;
import blue.origami.transpiler.TEnv;
import blue.origami.transpiler.TSkeleton;
import blue.origami.transpiler.TType;

public class TDeclCode extends TStaticAtomCode {

	public TDeclCode() {
		super(TType.tVoid);
	}

	@Override
	public TSkeleton getTemplate(TEnv env) {
		return TSkeleton.Null;
	}

	@Override
	public String strOut(TEnv env) {
		return "";
	}

	@Override
	public void emitCode(TEnv env, TCodeSection sec) {
	}

}
