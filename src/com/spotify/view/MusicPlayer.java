package com.spotify.view;

import java.awt.*;



import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.controller.SpotifyApiToken;
import com.spotify.model.Track;
import com.spotify.utli.Utilities;



import jaco.mp3.player.MP3Player;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.List;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

public class MusicPlayer extends JFrame {

	// UI components
	private JPanel contentPane;
	private JLabel movingLabel;
	private int x;
	JPanel panel_4;
	private  Timer timer1;
	public static MP3Player player = new MP3Player();

	private Map<Integer, AnimationInterface> animations;
	private boolean autoplay = false; // Default it to false



	// Variables for GuhDify's logic
	int m=0,k=0;
	private JTextField txtSongSearchTextField;
	private String textSongSearch="";
	private String textAllSearch;
	private JList list;
	private static int curruntPlayingTrack=0;
	Random random = new Random();
	Timer timer2;
	private boolean isPlaying = false;



	// Variables for animation
	int animationIndex;
	private Timer animationTimer = new Timer(10000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			animationIndex = (animationIndex + 1) % 9; // Increment animationIndex here
			panel_4.repaint();
		}
	});

	// Track last song and artist that was played
	private String lastSongTitle = "";
	private String lastArtistName = "";

	// Google speech recognition
	Microphone mic = new Microphone(FLACFileWriter.FLAC);
	GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	JButton btnNewButton_1;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicPlayer frame = new MusicPlayer(); //the application will be runned through the MusicPlayer function
					frame.initialize();
					frame.setVisible(true);
					SpotifyApiToken.getAccessToken(); //access Spotify API so the music's data can be fetched
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MusicPlayer() {

		//putting the animation from the animation classes
		animations = new HashMap<>();
		animations.put(0, new CircleAnimation());
		animations.put(1, new LineAnimation());
		animations.put(2, new RotatingHexagonAnimation());
		animations.put(3, new GridOfSquaresAnimation());
		animations.put(4, new RandomBarsAnimation());
		animations.put(5, new StarAnimation());
		animations.put(6, new CirclesAnimation());
		animations.put(7, new RandomSquaresAnimation());
		animations.put(8, new RandomTrianglesAnimation());

		timer1 = new Timer(1000 / 30, e -> panel_4.repaint());
		animationTimer.start();

		URL iconURL = getClass().getResource("/asset/Icon.jpg");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());


		duplex.setLanguage("en"); //language used for the speech to text API "en" = English


		player.setRepeat(true); // Setting the player to repeat and shuffle mode
		player.setShuffle(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 503);
		contentPane = new JPanel();
		contentPane.setBackground(Color.decode("#FCF6F5")); //Setting the color of the background
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false); // Make the size of the application to be unresizable



		setContentPane(contentPane);
		contentPane.setLayout(null);

		URL imageUrl = getClass().getResource("/asset/history.png"); //access the image asset
		ImageIcon imageIcon = new ImageIcon(imageUrl);

		//modify the size of the width and height
		int resizedWidth = 30;
		int resizedHeight = 30;

		Image image = imageIcon.getImage();
		Image resizedImage = image.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH); //the final resized image

		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		// Create a JButton and pass the resizedIcon to it
		JButton imageButton = new JButton(resizedIcon);
		imageButton.setBounds(770, 11, resizedWidth, resizedHeight); // Use resized dimensions here
		imageButton.setContentAreaFilled(false); // to make button transparent
		imageButton.setBorderPainted(false); // to remove border around the button



		imageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> allSongs = Files.readAllLines(Paths.get("playedSongs.txt"));
					//get all the song's that is stored in the playedSongs.txt file

					StringBuilder songs = new StringBuilder();

					int start = allSongs.size() > 5 ? allSongs.size() - 5 : 0;
					for (int i = start; i < allSongs.size(); i++) {
						songs.append(allSongs.get(i));
						songs.append("\n"); //appends all the songs in playedSongs.txt
					}

					// display a dialog with the last 5 songs or fewer if there are not enough
					JOptionPane.showMessageDialog(null, songs.toString(), "Recent Played Songs", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ex) {
					System.out.println("An error occurred while reading the file.");
					ex.printStackTrace();
				}
			}
		});


		contentPane.add(imageButton);
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.decode("#2F3C7E"), 5)); //set the color of the border of the big rectangle
		panel.setBackground(Color.decode("#FBEAEB")); //set the background color of the huge rectangle
		panel.setBounds(10, 13, 880, 100); //set the position
		panel.setLayout(null);

		panel.add(imageButton); // Add imageLabel to the panel
		contentPane.add(panel);




		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(Color.decode("#2F3C7E"), 2, true)); //border for the moving label area
		panel_5.setBackground(Color.decode("#FCF6F5"));//set bg color for the track rectangle
		panel_5.setBounds(40, 56, 807, 31);//set the position and size
		panel.add(panel_5);
		panel_5.setLayout(null);

		movingLabel = new JLabel("Track : "); //label writing inside panel_5
		movingLabel.setForeground(Color.decode("#2F3C7E")); //set fg color
		movingLabel.setFont(new Font("Tahoma", Font.BOLD, 18));//set font used
		movingLabel.setBackground(Color.decode("#FCF6F5"));//set bg color
		movingLabel.setBounds(10, 0, 803, 29);//set position and size
		panel_5.add(movingLabel);

		JPanel panel_6 = new JPanel(); //panel for title
		panel_6.setBorder(new LineBorder(new Color(255, 255, 255), 3, true));//set border
		panel_6.setBackground(Color.decode("#E2D1F9")); // set bg color
		panel_6.setBounds(365, 13, 390, 30); //set position and size
		panel.add(panel_6);
		panel_6.setLayout(null);

		JLabel lblNewLabel = new JLabel("GuhDify Music");
		lblNewLabel.setForeground(Color.decode("#2F3C7E"));
		lblNewLabel.setBackground(Color.decode("#2F3C7E"));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(130, 0, 190, 29);
		panel_6.add(lblNewLabel); //inserted inside panel_6

		JTextField txtSongSearchText = new JTextField(); //the search area
		txtSongSearchText.setBackground(Color.decode("#FCF6F5"));
		txtSongSearchText.setEditable(true); // make the field editable
		txtSongSearchText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSongSearchText.setForeground(Color.decode("#2F3C7E")); // change color to dark blue
		txtSongSearchText.setBounds(40, 13, 155, 30);
		panel.add(txtSongSearchText);
		txtSongSearchText.setColumns(10);



// Add a key listener to the text field
		// Add key listener to the search text field
		txtSongSearchText.addKeyListener(new KeyAdapter() {

			// Define what happens when a key is pressed
			public void keyPressed(KeyEvent e) {

				// If the key pressed is the Enter key
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					// Get the text in the search text field
					String searchText = txtSongSearchText.getText();

					// If there is any text entered
					if (searchText.length() > 0) {

						// Save the search text
						textSongSearch = searchText;

						// Search for the song based on the text entered
						searchSong();

						// Clear the list
						list.setListData(new String[0]);

						// If there are tracks found based on the search
						if (Utilities.alltracks.size() >= 1) {

							// Add each found track to the player's playlist
							for (Track track : Utilities.alltracks) {
								try {
									// Add the track to the playlist
									player.addToPlayList(new URL(track.getUrl()));

									// Update the list display with the names of the tracks
									list.setListData(Utilities.alltracks.stream().map(t->t.getTrackName()).toArray());
								} catch (MalformedURLException e1) {
									// Handle exception for malformed URL
									e1.printStackTrace();
								}
							}
						}
					} else {
						// Show an error message when no song name is entered in the search box
						JOptionPane.showMessageDialog(null, "Please enter a song to search.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		URL searchUrl = getClass().getResource("/asset/micro1.png");
		ImageIcon searchicon = new ImageIcon(searchUrl);
		DefaultListModel<String> model = new DefaultListModel<String>();
		btnNewButton_1 = new JButton("Speak",searchicon);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Create an executor to schedule tasks
				ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
				AtomicBoolean songIdentified = new AtomicBoolean(false);

				new Thread(() -> {
					try {
						duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}).start();

				// Disable the button to prevent further actions while processing
				btnNewButton_1.setEnabled(false);

				duplex.addResponseListener(new GSpeechResponseListener() {
					public void onResponse(GoogleResponse gr) {
						if (gr.isFinalResponse()) {
							songIdentified.set(true);
							duplex.stopSpeechRecognition(); // Stop the recognition here

							btnNewButton_1.setEnabled(true);
							textSongSearch = gr.getResponse(); // Get the response text and set it as the song search text
							System.out.println("Final : " + textSongSearch);

							if (textSongSearch.length() > 2) {
								txtSongSearchText.setText(textSongSearch);  // Make sure to use the same JTextField
								if (textSongSearch.length() < 1) {
									JOptionPane.showMessageDialog(null, "An error has occurred!", "Error", JOptionPane.ERROR_MESSAGE);
								} else {
									searchSong();
								}

								// Clear the list
								list.setListData(new String[0]);

								// If there are any tracks found
								if (Utilities.alltracks.size() >= 1) {

									// For each track
									for (Track track : Utilities.alltracks) {
										try {

											// Add the track to the player's playlist
											player.addToPlayList(new URL(track.getUrl()));

											// Update the list data with the track names
											list.setListData(Utilities.alltracks.stream().map(t->t.getTrackName()).toArray());
										} catch (MalformedURLException e1) {
											e1.printStackTrace();
										}
									}
								}
							}

							// Shutdown the executor when a song is identified or when there is a final response
							executor.shutdown();
						} else if (executor.isShutdown() && !songIdentified.get()) {
							duplex.stopSpeechRecognition();
							return;
						}
					}

				});


				// Schedule a task to be executed in 5 seconds
				executor.schedule(() -> {
					if (!songIdentified.get()) {
						SwingUtilities.invokeLater(() -> {
							duplex.stopSpeechRecognition();
							btnNewButton_1.setEnabled(true);
							JOptionPane.showMessageDialog(null, "No audio detected", "Error", JOptionPane.ERROR_MESSAGE);
						});
					}
				}, 8, TimeUnit.SECONDS);
			}
		});



		btnNewButton_1.setBackground(Color.decode("#E2D1F9"));
		btnNewButton_1.setBounds(207, 12, 124, 31);
		panel.add(btnNewButton_1);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.decode("#FBEAEB"));
		panel_1.setBackground(Color.decode("#FBEAEB"));
		panel_1.setBorder(new LineBorder(Color.decode("#2F3C7E"), 4, true));
		panel_1.setBounds(20, 383, 545, 60);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		URL iconUrl = getClass().getResource("/asset/prev1.png");
		ImageIcon originalIcon = new ImageIcon(iconUrl);

		// Get the Image from the ImageIcon
		Image originalImage = originalIcon.getImage();

		// Resize the Image
		int newWidth = 25; // Desired width
		int newHeight = 25; // Desired height
		Image resizedImage1 = originalImage.getScaledInstance(newWidth, newHeight,  Image.SCALE_SMOOTH);

		// Create a new ImageIcon from the resized image
		ImageIcon resizedIcon1 = new ImageIcon(resizedImage1);

		JButton btnNewButton = new JButton("",resizedIcon1);
		btnNewButton.setBackground(Color.decode("#E2D1F9"));
		btnNewButton.setForeground(Color.decode("#E2D1F9"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// If the current playing track index is greater than or equal to 1
					if(curruntPlayingTrack>=1) {
						// Decrement the current track index
						curruntPlayingTrack--;
						// Skip to the previous track in the player
						player.skipBackward();

					}
					System.out.println("CURRUNT : "+curruntPlayingTrack);
					// If the timer2 is running
					if(timer2.isRunning())
						timer2.stop();// Stop the timer2

					// Update the moving text panel with the current track's artist name and track name
					MovingTextPanel(Utilities.alltracks.get(curruntPlayingTrack).getArtistName()+" , "+Utilities.alltracks.get(curruntPlayingTrack).trackName);



				}catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Your Play List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setSelectedIcon(null);
		btnNewButton.setBounds(44, 13, 74, 36);
		panel_1.add(btnNewButton);
		URL pauseUrl = getClass().getResource("/asset/pause.png");
		ImageIcon originalPauseIcon = new ImageIcon(pauseUrl);

		// Get the Image from the ImageIcon
		Image originalPauseImage = originalPauseIcon.getImage();

		int newWidth1 = 30;  // Desired width
		int newHeight1 = 30; // Desired height
		Image resizedPauseImage = originalPauseImage.getScaledInstance(newWidth1, newHeight1,  Image.SCALE_SMOOTH); // Resize the pause Image


		// Create a new ImageIcon from the resized image
		ImageIcon resizedPauseIcon = new ImageIcon(resizedPauseImage);

		JButton button = new JButton("", resizedPauseIcon);
		button.setBackground(Color.decode("#E2D1F9"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.pause(); // Pause the player

				if(timer1.isRunning())
					timer1.stop();

			}
		});
		button.setForeground(Color.decode("#E2D1F9"));
		button.setBounds(140, 13, 74, 36);
		panel_1.add(button);

		URL playUrl = getClass().getResource("/asset/play.png");
		ImageIcon originalPlayIcon = new ImageIcon(playUrl);

		// Get the Image from the ImageIcon
		Image originalPlayImage = originalPlayIcon.getImage();

		// Resize the Image
		int newWidth2 = 30;  // Desired width
		int newHeight2 = 30; // Desired height
		Image resizedPlayImage = originalPlayImage.getScaledInstance(newWidth2, newHeight2,  Image.SCALE_SMOOTH);

		// Create a new ImageIcon from the resized image
		ImageIcon resizedPlayIcon = new ImageIcon(resizedPlayImage);

		JButton button_1 = new JButton("", resizedPlayIcon);
		button_1.setBackground(Color.decode("#E2D1F9"));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("CURRUNT : "+curruntPlayingTrack);

					// Get the song title and artist name of the current track
					String songTitle = Utilities.alltracks.get(curruntPlayingTrack).getTrackName();
					String artistName = Utilities.alltracks.get(curruntPlayingTrack).getArtistName();

					// Update the moving text panel with the current track's artist name and track name
					MovingTextPanel(artistName+" , "+songTitle);

					// Generate a random animation index
					animationIndex = random.nextInt(10);
					if(timer1.isRunning())
						timer1.restart();
					else
						timer1.start();
					if (!animationTimer.isRunning()) {
						animationTimer.start();
					}

					// Stop the currently playing song
					player.stop();

					// Clear the player's playlist before adding the new track
					player.getPlayList().clear();

					// Add new track to player
					try {
						player.addToPlayList(new URL(Utilities.alltracks.get(curruntPlayingTrack).getUrl()));
					} catch (MalformedURLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Invalid track URL", "Error", JOptionPane.ERROR_MESSAGE);
					}

					// Start playing the new song
					player.play();

					// Log the song play
					logSongPlay(songTitle, artistName);


				}catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Your Play List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});



		button_1.setForeground(Color.decode("#E2D1F9"));
		button_1.setBounds(236, 13, 74, 36);
		panel_1.add(button_1);
		URL stopUrl = getClass().getResource("/asset/stop.png");
		ImageIcon originalStopIcon = new ImageIcon(stopUrl);

		// Get the Image from the ImageIcon
		Image originalStopImage = originalStopIcon.getImage();

		// Resize the Image
		int newWidth3 = 30;  // Desired width
		int newHeight3 = 30; // Desired height
		Image resizedStopImage = originalStopImage.getScaledInstance(newWidth3, newHeight3,  Image.SCALE_SMOOTH);

		// Create a new ImageIcon from the resized image
		ImageIcon resizedStopIcon = new ImageIcon(resizedStopImage);

		JButton button_2 = new JButton("", resizedStopIcon);
		button_2.setBackground(Color.decode("#E2D1F9"));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Stop the player
				player.stop();
				// Stop the animation
				if (animationTimer.isRunning()) {
					animationTimer.stop();
				}
			}
		});
		button_2.setForeground(Color.decode("#E2D1F9"));
		button_2.setBounds(333, 13, 74, 36);
		panel_1.add(button_2);

		URL nextUrl = getClass().getResource("/asset/next.png");
		ImageIcon originalNextIcon = new ImageIcon(nextUrl);

		// Get the Image from the ImageIcon
		Image originalNextImage = originalNextIcon.getImage();

		// Resize the Image
		int newWidth4 = 30;  // Desired width
		int newHeight4 = 30; // Desired height
		Image resizedNextImage = originalNextImage.getScaledInstance(newWidth4, newHeight4,  Image.SCALE_SMOOTH);

		// Create a new ImageIcon from the resized image
		ImageIcon resizedNextIcon = new ImageIcon(resizedNextImage);

		JButton button_3 = new JButton("", resizedNextIcon);
		button_3.setBackground(Color.decode("#E2D1F9"));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(Utilities.alltracks.size()-1 > curruntPlayingTrack) {
						// Stop the currently playing song
						player.stop();

						// Clear the player's playlist
						player.getPlayList().clear();

						// Update current track index
						curruntPlayingTrack++;

						// Add next track to player
						try {
							player.addToPlayList(new URL(Utilities.alltracks.get(curruntPlayingTrack).getUrl()));
						} catch (MalformedURLException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Invalid track URL", "Error", JOptionPane.ERROR_MESSAGE);
						}

						// Start playing the new song
						player.play();



						// Display new song information on MovingTextPanel
						String songTitle = Utilities.alltracks.get(curruntPlayingTrack).getTrackName();
						String artistName = Utilities.alltracks.get(curruntPlayingTrack).getArtistName();
						MovingTextPanel(artistName+" , "+songTitle);
						logSongPlay(songTitle, artistName);

					}
					//...
				} catch (Exception e3) {
					e3.printStackTrace();
					JOptionPane.showMessageDialog(null, "Your Play List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(curruntPlayingTrack >= 1) {
						// Stop the currently playing song
						player.stop();

						// Clear the player's playlist
						player.getPlayList().clear();

						// Update current track index
						curruntPlayingTrack--;

						// Add previous track to player
						try {
							player.addToPlayList(new URL(Utilities.alltracks.get(curruntPlayingTrack).getUrl()));
						} catch (MalformedURLException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Invalid track URL", "Error", JOptionPane.ERROR_MESSAGE);
						}

						// Start playing the new song
						player.play();

						// Display new song information on MovingTextPanel
						String songTitle = Utilities.alltracks.get(curruntPlayingTrack).getTrackName();
						String artistName = Utilities.alltracks.get(curruntPlayingTrack).getArtistName();
						MovingTextPanel(artistName+" , "+songTitle);
						logSongPlay(songTitle, artistName);

					}
					//...
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Your Play List Is Empty", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});


		button_3.setForeground(Color.decode("#E2D1F9"));
		button_3.setBounds(431, 13, 74, 36);
		panel_1.add(button_3);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.decode("#FBEAEB"));
		panel_3.setBorder(new LineBorder(Color.decode("#FCF6F5"), 4));
		panel_3.setBounds(577, 126, 313, 317);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 289, 291);
		panel_3.add(scrollPane);

		list = new JList(model);
		list.setBackground(Color.decode("#FCF6F5"));
		scrollPane.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			// Override the getSize() method to return the length of 'values' array
			public int getSize() {
				return values.length;
			}

			// Override the getElementAt() method to return the element at the specified index in the 'values' array
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(list.getSelectedValue());

				int index = list.getSelectedIndex();

				String message = "<html><body>" +
						"<p>Track Name : " + Utilities.alltracks.get(index).getTrackName() + "</p>" +
						"<p>Artist Name : " + Utilities.alltracks.get(index).getArtistName() + "</p>" +
						"</body></html>";
				JOptionPane.showMessageDialog(	null, message, "Track Info", JOptionPane.INFORMATION_MESSAGE);

				System.out.println(index);
				MovingTextPanel(Utilities.alltracks.get(index).getArtistName() + " , " + Utilities.alltracks.get(index).getTrackName());

				// Stop the currently playing song
				player.stop();

				// Clear the player's playlist before adding the new track
				player.getPlayList().clear();

				// Update current track index
				curruntPlayingTrack = index;

				// Add new track to player
				try {
					player.addToPlayList(new URL(Utilities.alltracks.get(index).getUrl()));
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Invalid track URL", "Error", JOptionPane.ERROR_MESSAGE);
				}

				// Start playing the new song
				try {
					player.play();
					animationIndex = random.nextInt(10);
					if(timer1.isRunning())
						timer1.restart();
					else
						timer1.start();
					if (!animationTimer.isRunning()) {
						animationTimer.start();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error playing song", "Error", JOptionPane.ERROR_MESSAGE);
				}
				String songTitle = Utilities.alltracks.get(index).getTrackName();
				String artistName = Utilities.alltracks.get(index).getArtistName();
				logSongPlay(songTitle, artistName);
			}
		});


		panel_4 = new JPanel() {
			@Override
			// Override the paintComponent method
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				AnimationInterface currentAnimation = animations.get(animationIndex);// Get the current animation
				if (currentAnimation != null) { // If a valid animation exists, draw it on the panel
					currentAnimation.drawAnimation(g2d, getWidth(), getHeight(), random);
				}
			}

		};
			panel_4.setBackground(Color.decode("#FCF6F5"));
			panel_4.setBorder(new LineBorder(Color.decode("#2F3C7E"), 4));

			panel_4.setBounds(20, 126, 545, 240);
			contentPane.add(panel_4);

			timer1 = new Timer(1000 / 30, e -> panel_4.repaint());

	}

	public void MovingTextPanel(String text) {
		try {
			movingLabel.setText(text);// Set the text to be displayed on the movingLabel
			if (timer2 != null) {
				timer2.stop();// Stop the timer if it is already running
			}
			x = getWidth();
			timer2  = new Timer(2, e -> {
				x--;// Decrement the x-coordinate to move the label horizontally
				if (x < -movingLabel.getWidth()) {
					x = getWidth();// If the label has moved completely off the panel, reset its position
				}
				movingLabel.setLocation(x, 0);
			});
			timer2.start();// Start the timer to initiate the label movement
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



	private void searchSong() {
		CloseableHttpClient client = HttpClients.createDefault();

		String query = textSongSearch; // The search query for the song
		String type = "track"; // The type of search (in this case, searching for tracks)

		// Create the URL for the Spotify search API
		String url = "https://api.spotify.com/v1/search?q=" + URLEncoder.encode(query) + "&type=" + type;

		HttpGet request = new HttpGet(url);
		request.addHeader("Authorization", "Bearer " + Utilities.accesToke);

		String trackName, artistName = null, preview_url = null;

		if (Utilities.alltracks.size() > 0) {
			Utilities.alltracks.clear(); // Clear the list if it is not empty
		} else {
			System.out.println("list is empty");
		}

		try (CloseableHttpResponse response = client.execute(request)) {
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(responseString);

			// Extract the "tracks" node from the JSON response
			JsonNode tracksNode = rootNode.path("tracks").path("items");

			int trackIndex = 0;
			for (JsonNode trackNode : tracksNode) {
				trackName = trackNode.path("name").asText();
				String trackId = trackNode.path("id").asText();

				// Extract the "artists" node from the current track node
				JsonNode artistsNode = trackNode.path("artists");
				for (JsonNode artistNode : artistsNode) {
					artistName = artistNode.path("name").asText();
					String spotifyUrl = artistNode.path("external_urls").path("spotify").asText();
				}

				// Extract the "preview_url" node from the current track node
				JsonNode preview_urls = trackNode.path("preview_url");
				for (JsonNode item : preview_urls) {
					preview_url = item.path("preview_url").asText();
				}

				preview_url = trackNode.path("preview_url").asText();
				Track track = new Track(trackName, artistName, preview_url);

				// Add the track to the alltracks list
				Utilities.alltracks.add(track);

				// Set the curruntPlayingTrack to the first result
				if (trackIndex == 0) {
					curruntPlayingTrack = Utilities.alltracks.indexOf(track);
					trackIndex++;
				}
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}


	public void initialize() {
		try {
			List<String> allLines = Files.readAllLines(Paths.get("playedSongs.txt"));

			if (!allLines.isEmpty()) {
				String lastLine = allLines.get(allLines.size() - 1);

				// Split the last line into song title and artist name using " - " as the delimiter
				String[] split = lastLine.split(" - ");
				lastSongTitle = split[0].trim(); // Extract the last song title
				lastArtistName = split[1].trim(); // Extract the last artist name
			}
		} catch (IOException e) {
			System.out.println("An error occurred while reading from file.");
			e.printStackTrace();
		}
	}

	public void logSongPlay(String songTitle, String artistName) {
		// Check if the current song and artist are different from the last logged song
		if (!songTitle.trim().equals(lastSongTitle) || !artistName.trim().equals(lastArtistName)) {
			try {
				FileWriter writer = new FileWriter("playedSongs.txt", true); // Open file in append mode
				writer.write(songTitle + " - " + artistName + "\n"); // Write the song and artist to a new line
				writer.close();

				// Update the last song played with the current song and artist
				lastSongTitle = songTitle;
				lastArtistName = artistName;
			} catch (IOException e) {
				System.out.println("An error occurred while writing to file.");
				e.printStackTrace();
			}
		}
	}



}


