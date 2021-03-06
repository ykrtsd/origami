package blue.origami.transpiler.rule;

import blue.origami.transpiler.AST;
import blue.origami.transpiler.Env;
import blue.origami.transpiler.code.Code;
import blue.origami.transpiler.code.TupleCode;

public class TupleExpr extends LoggerRule implements Symbols, ParseRule {

	@Override
	public Code apply(Env env, AST t) {
		return new TupleCode(t.subMap(sub -> env.parseCode(env, sub), Code[]::new));
	}

}