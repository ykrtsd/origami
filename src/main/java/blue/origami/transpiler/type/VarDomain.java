package blue.origami.transpiler.type;

import blue.origami.transpiler.NameHint;
import blue.origami.transpiler.code.CastCode;

public class VarDomain {

	private VarTy[] dom;
	private String[] names;
	private int len = 0;

	private VarDomain(int n) {
		this.dom = new VarTy[n];
		this.names = new String[n];
		this.len = 0;
	}

	public <T> VarDomain(T[] a) {
		this(a.length + 4);
	}

	private VarTy newVarTy(String name, int id) {
		String var = String.valueOf((char) ('a' + this.len));
		VarTy ty = this.useMemo ? var(this.len) : new VarTy(var, id);
		this.dom[this.len] = ty;
		this.names[this.len] = name == null ? var : name;
		this.len++;
		return ty;
	}

	public Ty[] paramTypes(String[] names, Ty[] paramTypes) {
		Ty[] p = paramTypes.clone();
		for (int i = 0; i < paramTypes.length; i++) {
			if (p[i].isNULL()) {
				p[i] = this.newVarTy(null, -1);
			} else {
				p[i] = p[i].dupVar(this);
			}

		}
		return p;
	}

	public Ty retType(Ty retType) {
		return retType.isNULL() ? this.newVarTy(null, -1) : retType.dupVar(this);
	}

	public Ty resolvedAt(int index) {
		return this.dom[index].finalTy();
	}

	public static Ty newVarTy(VarDomain dom, String name) {
		return dom == null ? Ty.tAny : dom.newVarTy(name);
	}

	public Ty newVarTy(String name) {
		String n = this.useMemo ? name : NameHint.safeName(name);
		// ODebug.trace("%s -> %s", name, n);
		for (int i = 0; i < this.len; i++) {
			if (this.names[i].equals(n)) {
				return this.dom[i];
			}
		}
		return this.newVarTy(n, -1);
	}

	public Ty[] dupParamTypes(Ty[] g) {
		Ty[] v = new Ty[g.length];
		for (int i = 0; i < g.length; i++) {
			v[i] = g[i].dupVar(this);
		}
		return v;
	}

	public Ty[] dupParamTypes(Ty[] dParamTypes, Ty[] codeTy) {
		Ty[] gParamTypes = new Ty[dParamTypes.length];
		for (int i = 0; i < dParamTypes.length; i++) {
			gParamTypes[i] = dParamTypes[i].dupVar(this);
		}
		if (codeTy != null) {
			for (int i = 0; i < dParamTypes.length; i++) {
				// ODebug.trace("[%d] %s as %s %s", i, codeTy[i], dParamTypes[i],
				// gParamTypes[i]);
				gParamTypes[i].acceptTy(true, codeTy[i], VarLogger.Update);
			}
			for (int i = 0; i < dParamTypes.length; i++) {
				gParamTypes[i] = gParamTypes[i].finalTy();
			}
		}
		return gParamTypes;
	}

	public Ty dupRetType(Ty ret) {
		return ret.dupVar(this);
	}

	public int mapCost() {
		int mapCost = 0;
		for (int i = 0; i < this.len; i++) {
			Ty t = this.dom[i].finalTy();
			if (t == this.dom[i]) {
				mapCost += CastCode.STUPID;
			}
			if (t == Ty.tBool || t == Ty.tInt || t == Ty.tFloat) {
				mapCost += CastCode.CAST;
			}
		}
		return mapCost;
	}

	private boolean useMemo = false;

	public void useMemo() {
		this.len = 0;
		this.useMemo = true;
	}

	public int usedVars() {
		return this.len;
	}

	static VarTy[] memoed = { new VarTy("a", 0), new VarTy("b", 1), new VarTy("c", 2), new VarTy("d", 3),
			new VarTy("e", 4), new VarTy("f", 5), };

	public static VarTy var(int n) {
		return memoed[n];
	}

	public static Ty rename(Ty t) {
		VarDomain dom = new VarDomain(memoed);
		dom.useMemo();
		return dom.dupRetType(t);
	}

}
