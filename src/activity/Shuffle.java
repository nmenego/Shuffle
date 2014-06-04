package activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nmenego.shuffle.R;

/**
 * Shuffles mp3 files by renaming files. Use case: My car stereo can read MP3
 * files from an SD card, but lacks the much-needed shuffle feature. I use this
 * app to shuffle music from my SD card.
 * 
 * TODO 1 provide chosen directory to shuffle via a directory picker<br>
 * TODO 2 save changes in a log file for revert
 * 
 * @author nicomartin.enego
 * 
 */
public class Shuffle extends Activity {

	private static File mPath = Environment.getExternalStorageDirectory();
	private static final String FTYPE = ".mp3";
	private static final String[] dirs = { "//music", "//media//audio",
			"//audio" };
	private File mDir;
	private static TextView tv;
	private static Button shuffle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shuffle);

		shuffle = (Button) findViewById(R.id.bShuffle);
		tv = (TextView) findViewById(R.id.tResult);

		shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mPath == null || !mPath.exists()) {
					Log.d("onClick", "Path not found.");
					tv.setText("Path not found.");
					return;
				} else {
					Log.d("onClick",
							"Getting files in: " + mPath.getAbsolutePath());
				}

				// get files in provided directories
				for (int index = 0; index < dirs.length; index++) {
					File dir = new File(mPath.getAbsolutePath() + dirs[index]);
					// if directory exists in SD card
					if (dir.exists() && dir.isDirectory()) {
						mDir = dir;
						break;
					}
				}

				if (mDir == null || !mDir.exists()) {
					Log.d("onClick", "Path not found.");
					tv.setText("Path not found.");
					return;
				} else {
					Log.d("onClick", "Chosen path: " + mDir.getAbsolutePath());
				}

				File[] filesInDir = mDir.listFiles();

				// rename files...
				int fileCount = 0;
				for (File file : filesInDir) {
					String name = file.getName();
					String newName = getRandomText() + FTYPE;
					String newPath = mDir.getAbsolutePath() + "//" + newName;

					boolean renamed = file.renameTo(new File(newPath));
					if (renamed) {
						Log.d("onClick", name + " changed to " + newName);

						fileCount++;
					} else {
						// not supported when renaming fails.
					}
				}

				tv.setText("renamed files count: " + fileCount);
			}
		});
	}

	/**
	 * Generate random filename.
	 * 
	 * @return
	 */
	public static String getRandomText() {
		// get timestamp
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss-SSS",
				Locale.ENGLISH);
		String timeStampStr = sdf.format(new Date());

		// get random num
		Random randNum = new java.util.Random();
		String randNumStr = String.valueOf(randNum.nextInt(9999));

		return timeStampStr + randNumStr;
	}

}
