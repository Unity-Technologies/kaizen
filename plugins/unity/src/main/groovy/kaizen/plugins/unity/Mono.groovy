package kaizen.plugins.unity

import kaizen.plugins.clr.Clr

public interface Mono extends Clr {
	String lib(String profile, String fileName)
}