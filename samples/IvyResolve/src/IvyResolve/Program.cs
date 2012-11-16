using org.apache.ivy.core;
using org.apache.ivy.core.settings;
using org.apache.ivy.plugins.resolver;
using org.apache.ivy.plugins.repository.file;
using System;

public class Program
{
  public static void Main()
  {
    Console.WriteLine(IvyArtifactResolver.ResolveArtifact("kaizen", "kaizen", "0.2.3"));
  }
}

