package kaizen.plugins.clr

public interface ClrProvider<T extends Clr> {
	T runtimeForFrameworkVersion(String frameworkVersion)
}
