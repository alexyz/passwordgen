using System.IO;

// set PATH=%PATH%;C:\Windows\Microsoft.NET\Framework64\v4.0.30319\
// csc.exe gen.cs
public class Gen
{
	public static void Main(string[] args)
	{
		foreach (string arg in args) {
			byte[] a = File.ReadAllBytes(arg);
            byte[] hash;
            using (System.Security.Cryptography.MD5 md5 = System.Security.Cryptography.MD5.Create()) {
				hash = md5.ComputeHash(a);
            }
			using (StreamWriter sw = File.CreateText(arg.Replace(".", "") + ".cs")) {
                sw.WriteLine("public class Out {");
                sw.WriteLine("public const string Name = \"" + arg + "\";");
                WriteArray(sw, "Hash", hash);
				WriteArray(sw, "Data", a);
				sw.WriteLine("}");
            }
			return;
		}
	}

    private static void WriteArray (StreamWriter sw, string name, byte[] a) {
        sw.WriteLine("public static byte[] " + name + " = {");
		for (int n = 0; n < a.Length; n++) {
			sw.Write("0x" + a[n].ToString("X2") + ", ");
            if (((n+1) % 16) == 0) {
                sw.WriteLine();
            }
		}
        sw.WriteLine();
        sw.WriteLine("};");
    }
}
