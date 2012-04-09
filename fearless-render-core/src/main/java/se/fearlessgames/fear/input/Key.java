package se.fearlessgames.fear.input;

import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public enum Key {
	ZERO(Keyboard.KEY_0), //
	ONE(Keyboard.KEY_1), //
	TWO(Keyboard.KEY_2), //
	THREE(Keyboard.KEY_3), //
	FOUR(Keyboard.KEY_4), //
	FIVE(Keyboard.KEY_5), //
	SIX(Keyboard.KEY_6), //
	SEVEN(Keyboard.KEY_7), //
	EIGHT(Keyboard.KEY_8), //
	NINE(Keyboard.KEY_9), //
	A(Keyboard.KEY_A), //
	ADD(Keyboard.KEY_ADD), //
	APOSTROPHE(Keyboard.KEY_APOSTROPHE), //
	APPS(Keyboard.KEY_APPS), //
	AT(Keyboard.KEY_AT), //
	AX(Keyboard.KEY_AX), //
	B(Keyboard.KEY_B), //
	BACK(Keyboard.KEY_BACK), //
	BACKSLASH(Keyboard.KEY_BACKSLASH), //
	C(Keyboard.KEY_C), //
	CAPITAL(Keyboard.KEY_CAPITAL), //
	CIRCUMFLEX(Keyboard.KEY_CIRCUMFLEX), //
	COLON(Keyboard.KEY_COLON), //
	COMMA(Keyboard.KEY_COMMA), //
	CONVERT(Keyboard.KEY_CONVERT), //
	D(Keyboard.KEY_D), //
	DECIMAL(Keyboard.KEY_DECIMAL), //
	DELETE(Keyboard.KEY_DELETE), //
	DIVIDE(Keyboard.KEY_DIVIDE), //
	DOWN(Keyboard.KEY_DOWN), //
	E(Keyboard.KEY_E), //
	END(Keyboard.KEY_END), //
	EQUALS(Keyboard.KEY_EQUALS), //
	ESCAPE(Keyboard.KEY_ESCAPE), //
	F(Keyboard.KEY_F), //
	F1(Keyboard.KEY_F1), //
	F2(Keyboard.KEY_F2), //
	F3(Keyboard.KEY_F3), //
	F4(Keyboard.KEY_F4), //
	F5(Keyboard.KEY_F5), //
	F6(Keyboard.KEY_F6), //
	F7(Keyboard.KEY_F7), //
	F8(Keyboard.KEY_F8), //
	F9(Keyboard.KEY_F9), //
	F10(Keyboard.KEY_F10), //
	F11(Keyboard.KEY_F11), //
	F12(Keyboard.KEY_F12), //
	F13(Keyboard.KEY_F13), //
	F14(Keyboard.KEY_F14), //
	F15(Keyboard.KEY_F15), //
	G(Keyboard.KEY_G), //
	GRAVE(Keyboard.KEY_GRAVE), //
	H(Keyboard.KEY_H), //
	HOME(Keyboard.KEY_HOME), //
	I(Keyboard.KEY_I), //
	INSERT(Keyboard.KEY_INSERT), //
	J(Keyboard.KEY_J), //
	K(Keyboard.KEY_K), //
	KANA(Keyboard.KEY_KANA), //
	KANJI(Keyboard.KEY_KANJI), //
	L(Keyboard.KEY_L), //
	LBRACKET(Keyboard.KEY_LBRACKET), //
	LCONTROL(Keyboard.KEY_LCONTROL), //
	LEFT(Keyboard.KEY_LEFT), //
	LMENU(Keyboard.KEY_LMENU), //
	LMETA(Keyboard.KEY_LMETA), //
	LSHIFT(Keyboard.KEY_LSHIFT), //
	M(Keyboard.KEY_M), //
	MINUS(Keyboard.KEY_MINUS), //
	MULTIPLY(Keyboard.KEY_MULTIPLY), //
	N(Keyboard.KEY_N), //
	NEXT(Keyboard.KEY_NEXT), //
	NOCONVERT(Keyboard.KEY_NOCONVERT), //
	NUMLOCK(Keyboard.KEY_NUMLOCK), //
	NUMPAD0(Keyboard.KEY_NUMPAD0), //
	NUMPAD1(Keyboard.KEY_NUMPAD1), //
	NUMPAD2(Keyboard.KEY_NUMPAD2), //
	NUMPAD3(Keyboard.KEY_NUMPAD3), //
	NUMPAD4(Keyboard.KEY_NUMPAD4), //
	NUMPAD5(Keyboard.KEY_NUMPAD5), //
	NUMPAD6(Keyboard.KEY_NUMPAD6), //
	NUMPAD7(Keyboard.KEY_NUMPAD7), //
	NUMPAD8(Keyboard.KEY_NUMPAD8), //
	NUMPAD9(Keyboard.KEY_NUMPAD9), //
	NUMPADCOMMA(Keyboard.KEY_NUMPADCOMMA), //
	NUMPADENTER(Keyboard.KEY_NUMPADENTER), //
	NUMPADEQUALS(Keyboard.KEY_NUMPADEQUALS), //
	O(Keyboard.KEY_O), //
	P(Keyboard.KEY_P), //
	PAUSE(Keyboard.KEY_PAUSE), //
	POWER(Keyboard.KEY_POWER), //
	PERIOD(Keyboard.KEY_PERIOD), //
	PRIOR(Keyboard.KEY_PRIOR), //
	Q(Keyboard.KEY_Q), //
	R(Keyboard.KEY_R), //
	RBRACKET(Keyboard.KEY_RBRACKET), //
	RCONTROL(Keyboard.KEY_RCONTROL), //
	RETURN(Keyboard.KEY_RETURN), //
	RIGHT(Keyboard.KEY_RIGHT), //
	RMENU(Keyboard.KEY_RMENU), //
	RMETA(Keyboard.KEY_RMETA), //
	RSHIFT(Keyboard.KEY_RSHIFT), //
	S(Keyboard.KEY_S), //
	SCROLL(Keyboard.KEY_SCROLL), //
	SEMICOLON(Keyboard.KEY_SEMICOLON), //
	SLASH(Keyboard.KEY_SLASH), //
	SLEEP(Keyboard.KEY_SLEEP), //
	SPACE(Keyboard.KEY_SPACE), //
	STOP(Keyboard.KEY_STOP), //
	NUMPADSUBTRACT(Keyboard.KEY_SUBTRACT),
	SYSRQ(Keyboard.KEY_SYSRQ), //
	T(Keyboard.KEY_T), //
	TAB(Keyboard.KEY_TAB), //
	U(Keyboard.KEY_U), //
	UNDERLINE(Keyboard.KEY_UNDERLINE), //
	UNLABELED(Keyboard.KEY_UNLABELED), //
	UP(Keyboard.KEY_UP), //
	V(Keyboard.KEY_V), //
	W(Keyboard.KEY_W), //
	X(Keyboard.KEY_X), //
	Y(Keyboard.KEY_Y), //
	YEN(Keyboard.KEY_YEN), //
	Z(Keyboard.KEY_Z), //
	NONE(Keyboard.KEY_NONE);

	private final int lwjglCode;

	private final static Map<Integer, Key> lookupMap = new HashMap<Integer, Key>();

	private Key(final int lwjglCode) {
		this.lwjglCode = lwjglCode;

	}

	public static Key findByKeyEventCode(int lwjglCode) {
		return lookupMap.get(lwjglCode);
	}

	static {
		for (Key key : Key.values()) {
			lookupMap.put(key.lwjglCode, key);
		}
	}

}
