package pwgen;

import java.util.AbstractList;

/** list wrapper for stringbuilder */
class StringBuilderList extends AbstractList<Character> {

	public final StringBuilder sb;

	public StringBuilderList (StringBuilder sb) {
		this.sb = sb;
	}

	@Override
	public boolean add(Character character) {
		sb.append(character.charValue());
		return true;
	}

	@Override
	public Character set (int index, Character element) {
		char c = sb.charAt(index);
		sb.setCharAt(index, element);
		return c;
	}

	@Override
	public Character remove(int index) {
		Character c = sb.charAt(index);
		sb.deleteCharAt(index);
		return c;
	}

	@Override
	public Character get (int index) {
		return sb.charAt(index);
	}

	@Override
	public int size () {
		return sb.length();
	}
	
}