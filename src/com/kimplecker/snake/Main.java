package com.kimplecker.snake;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		Player snake = new Player("Snake", 1, 1, 0);
		MapLoader mapLoader = new MapLoader();
		int[][] loadedMap = mapLoader.loadMap("maps/Map.txt");
		LeaderboardService leaderboardService = new LeaderboardService();

		Font titleFont = new Font("Harlow Solid Italic", Font.PLAIN, 70);
		Font uiFont = new Font("Arial", Font.BOLD, 30);
		Font leaderboardFont = new Font("Arial", Font.PLAIN, 24);

		JPanel mainMenu = new JPanel(new GridBagLayout());
		mainMenu.setBackground(Color.black);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel welcomeLabel = new JLabel("Welcome Player!");
		welcomeLabel.setFont(titleFont);
		welcomeLabel.setForeground(Color.white);
		mainMenu.add(welcomeLabel, gbc);

		gbc.gridy++;

		JLabel nameLabel = new JLabel("What is your name?");
		nameLabel.setFont(uiFont);
		nameLabel.setForeground(Color.white);
		mainMenu.add(nameLabel, gbc);

		gbc.gridy++;

		JTextField nameField = new JTextField(15);
		nameField.setForeground(Color.BLUE);
		nameField.setBackground(Color.WHITE);
		mainMenu.add(nameField, gbc);

		gbc.gridy++;

		JButton playButton = new JButton("Play");
		playButton.setForeground(Color.white);
		playButton.setBackground(Color.black);
		playButton.setBorder(null);
		playButton.setFont(uiFont);
		gbc.anchor = GridBagConstraints.WEST;
		playButton.addActionListener(e -> {
			setPlayerName(nameField.getText(), snake);
		});
		mainMenu.add(playButton, gbc);

		JButton leaderboardButton = new JButton("Leaderboard");
		leaderboardButton.setBorder(null);
		leaderboardButton.setForeground(Color.white);
		leaderboardButton.setBackground(Color.black);
		leaderboardButton.setFont(uiFont);
		gbc.anchor = GridBagConstraints.CENTER;
		mainMenu.add(leaderboardButton, gbc);

		JButton exitButton = new JButton("Exit");
		exitButton.setBorder(null);
		exitButton.setForeground(Color.white);
		exitButton.setBackground(Color.black);
		exitButton.setFont(uiFont);
		exitButton.addActionListener(e -> System.exit(0));
		gbc.anchor = GridBagConstraints.EAST;
		mainMenu.add(exitButton, gbc);

		JPanel gamePanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			private int CELL_SIZE = 25;

			@Override
			public void paintComponent(Graphics g) {
				setPreferredSize(new Dimension(loadedMap[0].length * CELL_SIZE, loadedMap.length * CELL_SIZE));
				super.paintComponent(g);
				for (int i = 0; i < loadedMap.length; i++) {
					for (int j = 0; j < loadedMap[0].length; j++) {
						if (loadedMap[i][j] == 0) { // Map
							g.setColor(Color.BLACK);
							g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						}
						if (loadedMap[i][j] == 1) { // Wall
							g.setColor(Color.GRAY);
							g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						}
						if (loadedMap[i][j] == 2) { // Food
							g.setColor(Color.BLUE);
							g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						}
						if (loadedMap[i][j] == 4) { // Player trail
							g.setColor(Color.GREEN);
							g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
						}
					}
				}
			}
		};

		CardLayout cardLayout = new CardLayout();
		JPanel cardPanel = new JPanel(cardLayout);

		// Leaderboard panel
		JPanel leaderboardPanel = new JPanel(new GridBagLayout());
		leaderboardPanel.setBackground(Color.black);

		GridBagConstraints leaderboardGbc = new GridBagConstraints();
		leaderboardGbc.gridx = 0;
		leaderboardGbc.gridy = 0;
		leaderboardGbc.insets = new Insets(10, 10, 10, 10);

		JLabel leaderboardTitle = new JLabel("Leaderboard");
		leaderboardTitle.setFont(titleFont);
		leaderboardTitle.setForeground(Color.white);
		leaderboardPanel.add(leaderboardTitle, leaderboardGbc);

		leaderboardGbc.gridy++;

		JLabel leaderboardContent = new JLabel();
		leaderboardContent.setFont(leaderboardFont);
		leaderboardContent.setForeground(Color.white);
		leaderboardPanel.add(leaderboardContent, leaderboardGbc);

		leaderboardGbc.gridy++;

		JButton backButton = new JButton("Back");
		backButton.setBorder(null);
		backButton.setForeground(Color.white);
		backButton.setBackground(Color.black);
		backButton.setFont(uiFont);
		leaderboardPanel.add(backButton, leaderboardGbc);

		cardPanel.add(mainMenu, "MainMenu");
		cardPanel.add(gamePanel, "Game");
		cardPanel.add(leaderboardPanel, "Leaderboard");

		gamePanel.setFocusable(true);

		gamePanel.addKeyListener(new KeyAdapter() {
			Timer timer = new Timer();
			Object[] options = { "Resume", "Quit", "Restart" };

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				boolean selected = false;

				if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT
						|| keyCode == KeyEvent.VK_RIGHT) {
					int code = getDirection(keyCode);
					startTimer(code);
				} else if (keyCode == KeyEvent.VK_ESCAPE) {
					timer.cancel();
					do {
						int choice = JOptionPane.showOptionDialog(
								null,
								"Game Paused",
								"Pause",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null,
								options,
								options[0]);

						if (choice == 0) {
							selected = true;

						} else if (choice == 1) {
							leaderboardService.insertScore(snake.getname(), snake.getCount());
							updateLeaderboardContent(leaderboardService, leaderboardContent, 10);
							cardLayout.show(cardPanel, "Leaderboard");
							selected = true;

						} else if (choice == 2) {
							snake.setPosX(1);
							snake.setPosY(1);
							loadedMap[1][1] = 4;

							for (int i = 0; i < loadedMap.length; i++) {
								for (int j = 0; j < loadedMap[0].length; j++) {
									if (!(i == 1 && j == 1) && loadedMap[i][j] != 1) {
										loadedMap[i][j] = 0;
									}
								}
							}

							snake.setCount(0);
							Movement.remove();
							gamePanel.repaint();
							selected = true;
						}
					} while (!selected);
				}
			}

			private void resetGameToMenu() {
				snake.setPosX(1);
				snake.setPosY(1);
				snake.setCount(0);

				for (int i = 0; i < loadedMap.length; i++) {
					for (int j = 0; j < loadedMap[0].length; j++) {
						if (loadedMap[i][j] != 1) {
							loadedMap[i][j] = 0;
						}
					}
				}

				loadedMap[1][1] = 4;
				Movement.remove();
				gamePanel.repaint();

				cardLayout.show(cardPanel, "MainMenu");
				nameField.setText(snake.getname());
				mainMenu.requestFocusInWindow();
			}

			private int getDirection(int keyCode) {
				switch (keyCode) {
				case KeyEvent.VK_UP:
					return 3;
				case KeyEvent.VK_DOWN:
					return 4;
				case KeyEvent.VK_LEFT:
					return 1;
				case KeyEvent.VK_RIGHT:
					return 2;
				default:
					return 0;
				}
			}

			private void spawnFood() {
				int posX = (int) (Math.random() * loadedMap.length);
				int posY = (int) (Math.random() * loadedMap[0].length);

				for (int i = 0; i < loadedMap.length; i++) {
					for (int j = 0; j < loadedMap[0].length; j++) {
						if (loadedMap[i][j] == 2) {
							return;
						}
					}
				}

				while (loadedMap[posX][posY] == 1 || loadedMap[posX][posY] == 4 || loadedMap[posX][posY] == 3) {
					posX = (int) (Math.random() * loadedMap.length);
					posY = (int) (Math.random() * loadedMap[0].length);
				}

				loadedMap[posX][posY] = 2;
			}

			private void startTimer(int code) {
				timer.cancel();
				int delay = 0;
				int count = Math.min(snake.getCount(), 300);
				int period = 400 - count;

				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						boolean lost = Movement.move(snake, code, loadedMap);

						if (lost) {
							timer.cancel();
							leaderboardService.insertScore(snake.getname(), snake.getCount());
							resetGameToMenu();
							return;
						}

						spawnFood();
						gamePanel.repaint();
					}
				}, delay, period);
			}
		});

		leaderboardButton.addActionListener(e -> {
			updateLeaderboardContent(leaderboardService, leaderboardContent, 10);
			cardLayout.show(cardPanel, "Leaderboard");
		});

		backButton.addActionListener(e -> {
			cardLayout.show(cardPanel, "MainMenu");
			nameField.requestFocusInWindow();
		});

		JFrame frame = new JFrame("Snake");
		frame.add(cardPanel, BorderLayout.CENTER);
		frame.setSize(1125, 625);
		frame.setBackground(Color.BLACK);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.revalidate();
		frame.repaint();

		playButton.addActionListener(e -> {
			cardLayout.show(cardPanel, "Game");
			gamePanel.requestFocusInWindow();
		});

		playButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					cardLayout.show(cardPanel, "Game");
					gamePanel.requestFocusInWindow();
				}
			}
		});

		exitButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.exit(0);
				}
			}
		});
	}

	private static void setPlayerName(String input, Player snake) {
		snake.setname(input);
	}

	private static void updateLeaderboardContent(
			LeaderboardService leaderboardService,
			JLabel leaderboardContent,
			int limit) {

		List<String> topScores = leaderboardService.getTopScores(limit);
		StringBuilder html = new StringBuilder("<html><div style='text-align:center;'>");

		if (topScores.isEmpty()) {
			html.append("No scores available yet.");
		} else {
			for (String score : topScores) {
				html.append(score).append("<br>");
			}
		}

		html.append("</div></html>");
		leaderboardContent.setText(html.toString());
	}
}