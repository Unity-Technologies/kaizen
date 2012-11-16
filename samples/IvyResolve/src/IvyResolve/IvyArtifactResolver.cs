// code adapted from
// http://developers-blog.org/blog/default/2010/11/08/Embed-Ivy-How-to-use-Ivy-with-Java 

using java.io;
using org.apache.ivy.core.settings;
using org.apache.ivy.plugins.resolver;
using org.apache.ivy;
using org.apache.ivy.core.module.descriptor;
using org.apache.ivy.core.module.id;
using org.apache.ivy.plugins.parser.xml;
using org.apache.ivy.core.resolve;
using org.apache.ivy.core.report;

public static class IvyArtifactResolver
{
  public static File ResolveArtifact(string groupId, string artifactId, string version)
  {
    var ivyFile = CreateIvyFileFor(groupId, artifactId, version);
    
    var ivy = CreateIvy();

    var confs = new string[] {"default"};
    var resolveOptions = new ResolveOptions().setConfs(confs);
    var report = ivy.resolve(ivyFile.toURL(), resolveOptions);
    
    var artifactFile = report.getAllArtifactsReports()[0].getLocalFile();
    return artifactFile;
  }

  static File CreateIvyFileFor(string groupId, string artifactId, string version)
  {
    var ivyFile = File.createTempFile("ivy", ".xml");
    ivyFile.deleteOnExit();

    var md = DefaultModuleDescriptor.newDefaultInstance(
      ModuleRevisionId.newInstance(groupId, artifactId + "-caller", "working"));
    var dd = new DefaultDependencyDescriptor(md,
      ModuleRevisionId.newInstance(groupId, artifactId, version), false, false, true);
    md.addDependency(dd);

    //creates an ivy configuration file
    XmlModuleDescriptorWriter.write(md, ivyFile);
    return ivyFile;
  }

  static Ivy CreateIvy()
  {
    var ivySettings = new IvySettings();
    var resolver = CreateResolver();
    ivySettings.addResolver(resolver);
    ivySettings.setDefaultResolver(resolver.getName());
    return  Ivy.newInstance(ivySettings);
  }

  static RepositoryResolver CreateResolver()
  {
    var resolver = new URLResolver();
    resolver.setName("kaizen");
    resolver.addArtifactPattern(
      "http://unity-technologies.github.com/kaizen/repositories/unstable/"
      + "[organisation]/[module]/[revision]/[artifact](-[revision]).[ext]");
    return resolver;
  }
}

