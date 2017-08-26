using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Windows.Forms;

// set PATH=%PATH%;C:\Windows\Microsoft.NET\Framework64\v4.0.30319\
// csc.exe /out:passwordgen.exe /target:winexe wrapper.cs passwordgenjar.cs
public class Wrapper {
	
	public static void Main(string[] args) {
		
		try {
			string p = System.IO.Path.Combine(Path.GetTempPath(), Out.Name);
			bool create = true;
	
			if (File.Exists(p)) {	
				Console.WriteLine("file exists");
				using (System.Security.Cryptography.MD5 md5 = System.Security.Cryptography.MD5.Create()) {
					using (var s = File.OpenRead(p)) {
						byte[] hash = md5.ComputeHash(s);
						if (hash.SequenceEqual(Out.Hash)) {
							Console.WriteLine("hashes equal");
							create = false;
						}
					}
				}
			}
			
			if (create) {
				Console.WriteLine("create file");
				using (FileStream f = new FileStream(p, FileMode.CreateNew)) {
					f.Write(Out.Data, 0, Out.Data.Length);
				}
			}
	
			Out.Data = null;

			Process pr = new Process();
            pr.StartInfo.UseShellExecute = false;
            pr.StartInfo.FileName = "javaw.exe";
			pr.StartInfo.Arguments = "-jar " + p;
            pr.Start();
			pr.WaitForExit();
			if (pr.ExitCode != 0) {
				throw new Exception("exit " + pr.ExitCode);
			}
		} catch (Exception e) {
			Console.WriteLine("exception: " + e);
			MessageBox.Show(e.ToString(), Out.Name, MessageBoxButtons.OK, MessageBoxIcon.Error);
		}
		
	}
	
}
