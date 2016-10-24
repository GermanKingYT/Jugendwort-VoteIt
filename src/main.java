import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Ari
 *
 */
public class main {

	public static int work = 0;
	public static int often = 0;
	public static int max = 0;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How often should I call the Vote each thread?");
		String s = br.readLine();
		often = Integer.parseInt(s);
		System.out.println("How many Threads?");
		int s2 = Integer.parseInt(br.readLine());
		max = often * s2;
		for (int i = 0; i < s2; i++)
		{
			System.out.println("Started Therad number: " + (i + 1) + "/" + s2);
			new VoteIt().start();
		}	
	}
}

class VoteIt extends Thread
{
	public void run()
	{
		try {
			for (int i = 0; i < main.often; i++)
			{
				String url = "http://www.jugendwort.de/wp-admin/admin-ajax.php";
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				//add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

				String urlParameters = "totalpoll%5Bid%5D=4081&totalpoll%5Bpage%5D=1&totalpoll%5Bview%5D=vote&totalpoll%5Bchoices%5D%5B%5D=18&totalpoll%5Baction%5D=vote&action=tp_action";

				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				int responseCode = con.getResponseCode();
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				//print result
				if (response.toString().contains("DEINE Teilnahme an unserem")) {
					main.work++;
					System.out.println("Vote Done: " + main.work + "/" + main.max);
				} else {
					System.out.println("Failed Vote: " + i);
					System.out.println(response.toString());
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
