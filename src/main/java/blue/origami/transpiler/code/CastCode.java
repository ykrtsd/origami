package blue.origami.transpiler.code;

import blue.origami.transpiler.TCodeSection;
import blue.origami.transpiler.CodeTemplate;
import blue.origami.transpiler.TEnv;
import blue.origami.transpiler.Template;
import blue.origami.transpiler.Ty;
import blue.origami.util.StringCombinator;

public class CastCode extends Code1 implements CallCode {
	public CastCode(Ty ret, TConvTemplate tp, Code inner) {
		super(ret, inner);
		this.setTemplate(tp);
	}

	private Template tp;

	public void setTemplate(Template tp) {
		this.tp = tp;
	}

	@Override
	public Template getTemplate() {
		return this.tp;
	}

	@Override
	public void emitCode(TEnv env, TCodeSection sec) {
		sec.pushCast(env, this);
	}

	@Override
	public void strOut(StringBuilder sb) {
		sb.append("(");
		StringCombinator.append(sb, this.getType());
		sb.append(")");
		StringCombinator.append(sb, this.getInner());
	}

	// constants
	public static final int SAME = 0;
	public static final int BESTCAST = 1;
	public static final int CAST = 3;
	public static final int BESTCONV = 8;
	public static final int CONV = 12;
	public static final int BADCONV = 64;
	public static final int DOWNCAST = 64;
	public static final int STUPID = 256;

	public static class TConvTemplate extends CodeTemplate {

		public static final TConvTemplate Stupid = new TConvTemplate("", Ty.tUntyped0, Ty.tUntyped0, STUPID, "%s");
		// fields
		private int mapCost;

		public TConvTemplate(String name, Ty fromType, Ty returnType, int mapCost, String template) {
			super(name, returnType, new Ty[] { fromType }, template);
			this.mapCost = mapCost;
		}

		public int mapCost() {
			return this.mapCost;
		}

	}

	// public static class TVarCastCode extends CastCode {
	// public TVarCastCode(Ty ret, Code inner) {
	// super(ret, null, inner);
	// }
	// }

	public static class TBoxCode extends CastCode {

		public TBoxCode(Ty ret, Code inner) {
			super(ret, null, inner);
		}

	}

	public static class TUnboxCode extends CastCode {

		public TUnboxCode(Ty ret, Code inner) {
			super(ret, null, inner);
		}

	}

}