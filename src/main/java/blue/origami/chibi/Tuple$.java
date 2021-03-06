package blue.origami.chibi;

import java.lang.reflect.Field;

import blue.origami.common.OConsole;
import blue.origami.common.OStrings;

public class Tuple$ implements OStrings, Cloneable {

	@Override
	public Tuple$ clone() {
		return this;
	}

	@Override
	public void strOut(StringBuilder sb) {
		sb.append("(");
		int cnt = 0;
		Field[] fs = this.getClass().getDeclaredFields();
		for (Field f : fs) {
			if (cnt > 0) {
				sb.append(",");
			}
			OStrings.appendQuoted(sb, this.getf(f, this));
			cnt++;
		}
		sb.append(")");
	}

	private Object getf(Field f, Object o) {
		try {
			return f.get(o);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			OConsole.exit(1, e);
			return null;
		}
	}

	@Override
	public String toString() {
		return OStrings.stringfy(this);
	}

}