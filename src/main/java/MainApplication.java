import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.Client.ClientState;
import com.turn.ttorrent.client.SharedTorrent;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class MainApplication {

	public static void main(String args[]) {
		String sharedFile = "/home/upadhyy3/bitTorrentTestFolder/test.pdf";

		try {
			// Tracker tracker = new Tracker(new
			// InetSocketAddress("192.168.56.1",6969));
			Tracker tracker = new Tracker(InetAddress.getLocalHost());
			tracker.start();
			System.out.println("Tracker running.");
			// Tracker tracker = new Tracker(new InetSocketAddress(49157));

			System.out.println("create new .torrent metainfo file...");
			Torrent torrent = Torrent.create(new File(sharedFile), tracker
					.getAnnounceUrl().toURI(), "createdByShivam");
			System.out.println(tracker.getAnnounceUrl().toURI());
			System.out.println("save .torrent to file...");

			// for (File f : new File(sharedFile).listFiles()) {
			// Torrent torrent = Torrent.create(f,
			// tracker.getAnnounceUrl().toURI(), "createdByShivam");
			// System.out.println(tracker.getAnnounceUrl());
			// System.out.println(tracker.hashCode());
			// FileOutputStream fos = new
			// FileOutputStream("G:\\TestFolderBitTorrent\\"+torrent.getName()+".torrent");
			// torrent.save( fos );
			// fos.close();
			// }

			FileOutputStream fos = new FileOutputStream(
					"/home/upadhyy3/bitTorrentTestFolder/seed.torrent");
			torrent.save(fos);
			fos.close();

			tracker.announce(new TrackedTorrent(torrent));
			Client seeder = new Client(InetAddress.getLocalHost(),
					new SharedTorrent(torrent, new File(
							"/home/upadhyy3/bitTorrentTestFolder"), true));
			seeder.share();
			while (!ClientState.SEEDING.equals(seeder.getState())) {
				// Check if there's an error
				if (ClientState.ERROR.equals(seeder.getState())) {
					try {
						throw new Exception("ttorrent client Error State");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// FilenameFilter filter = new FilenameFilter() {
			// public boolean accept(File dir, String name) {
			// return name.endsWith(".torrent");
			// }
			// };
			//
			// System.out.println("Announcing the torrent");
			// for (File f : new
			// File("G:\\TestFolderBitTorrent\\").listFiles(filter)) {
			// tracker.announce(TrackedTorrent.load(f));
			// System.out.println(tracker.getAnnounceUrl());
			// System.out.println(tracker.hashCode());
			// }
			// tracker.start();
			// TrackerTest trackerTest = new TrackerTest();
			// trackerTest.trackerTestMethod(tracker);

			// ClientInteface ci = new ClientInteface();
			// ci.testMethod();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
