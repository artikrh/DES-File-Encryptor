package des_encryption;

import java.awt.EventQueue;
import org.jdesktop.swingx.prompt.PromptSupport;
import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DES_Encryption {

	private JFrame frmDesFileEncryptor;
	private JTextField txtFile;
	private JTextField txtKey;

	public static void main(String[] args) {
		UIManager.put("Button.disabledText", new ColorUIResource(new Color(238,238,238)));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DES_Encryption window = new DES_Encryption();
					window.frmDesFileEncryptor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

	public void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[8];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.close();
		is.close();
	}

	public void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

	public void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

	public DES_Encryption() {
		initialize();
	}

	private void initialize() {
		frmDesFileEncryptor = new JFrame();
		frmDesFileEncryptor.setResizable(false);
		frmDesFileEncryptor.setIconImage(Toolkit.getDefaultToolkit().getImage(DES_Encryption.class.getResource("/img/frmIcon.png")));
		frmDesFileEncryptor.setTitle("DES File Encryptor");
		frmDesFileEncryptor.setBounds(100, 100, 960, 598);
		frmDesFileEncryptor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDesFileEncryptor.getContentPane().setLayout(null);

		final Color btnC = new Color(65,182,145);
		final Color btnHover = new Color(54,153,122);
		final Color UI = new Color(70,85,89);
		final Color ta = new Color(201,207,209);
		
		JPanel pnUI = new JPanel();
		pnUI.setBounds(0, 0, 956, 569);
		pnUI.setBackground(UI);
		frmDesFileEncryptor.getContentPane().add(pnUI);
		pnUI.setLayout(null);
		
		JTextArea taEncryptedFile = new JTextArea();
		taEncryptedFile.setEditable(false);
		taEncryptedFile.setBackground(ta);
		taEncryptedFile.setLineWrap(true);
		taEncryptedFile.setBounds(45, 254, 407, 271);
		pnUI.add(taEncryptedFile);
		
		JLabel lblMessage = new JLabel("Key must be at least 8 characters (64 bit)!");
		lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblMessage.setVisible(false);
		lblMessage.setForeground(new Color(255, 0, 0));
		lblMessage.setBounds(178, 22, 212, 15);
		pnUI.add(lblMessage);

		JLabel lblDecryptedFile = new JLabel("Decrypted file");
		lblDecryptedFile.setForeground(Color.WHITE);
		lblDecryptedFile.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblDecryptedFile.setBounds(792, 532, 117, 26);
		pnUI.add(lblDecryptedFile);

		JButton btnDekripto = new JButton("DECRYPT");
		btnDekripto.setEnabled(false);
		btnDekripto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnDekripto.setBackground(btnHover);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnDekripto.setBackground(btnC);
			}
		});
		btnDekripto.setIcon(null);
		btnDekripto.setBackground(btnC);
		btnDekripto.setFocusPainted(false);
		btnDekripto.setForeground(new Color(238,238,238));
		btnDekripto.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnDekripto.setBounds(502, 194, 407, 49);
		pnUI.add(btnDekripto);

		JLabel lblEncryptionKey = new JLabel("Encryption key");
		lblEncryptionKey.setForeground(Color.WHITE);
		lblEncryptionKey.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblEncryptionKey.setBounds(45, 43, 123, 26);
		pnUI.add(lblEncryptionKey);

		txtKey = new JTextField();
		txtKey.setToolTipText("DES Key");
		txtKey.setBackground(new Color(237,239,239));
		txtKey.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtKey.setColumns(10);
		txtKey.setBounds(178, 44, 136, 28);
		pnUI.add(txtKey);
		PromptSupport.setPrompt("Enter your DES key", txtKey);

		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.setBounds(324, 43, 128, 29);
		btnChooseFile.setBackground(btnC);
		btnChooseFile.setForeground(new Color(238,238,238));
		pnUI.add(btnChooseFile);

		txtFile = new JTextField();
		txtFile.setEditable(false);
		txtFile.setBackground(new Color(201,207,209));
		txtFile.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtFile.setBounds(178, 83, 274, 26);
		pnUI.add(txtFile);
		txtFile.setColumns(10);

		JLabel lblLoadFile = new JLabel("File path");
		lblLoadFile.setForeground(Color.WHITE);
		lblLoadFile.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblLoadFile.setBounds(45, 82, 72, 26);
		pnUI.add(lblLoadFile);

		JLabel lblEncryptedFile = new JLabel("Encrypted file");
		lblEncryptedFile.setForeground(Color.WHITE);
		lblEncryptedFile.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblEncryptedFile.setBounds(337, 532, 115, 26);
		pnUI.add(lblEncryptedFile);

		JLabel lblNewLabel = new JLabel("Raw file");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblNewLabel.setBounds(502, 11, 66, 26);
		pnUI.add(lblNewLabel);

		JTextArea taDecryptedFile = new JTextArea();
		taDecryptedFile.setEditable(false);
		taDecryptedFile.setBackground(ta);
		taDecryptedFile.setLineWrap(true);
		taDecryptedFile.setBounds(502, 254, 407, 271);
		pnUI.add(taDecryptedFile);

		JTextArea taOriginalFile = new JTextArea();
		taOriginalFile.setEditable(false);
		taOriginalFile.setBackground(ta);
		taOriginalFile.setLineWrap(true);
		taOriginalFile.setBounds(502, 43, 407, 117);
		pnUI.add(taOriginalFile);

		JButton btnEnkripto = new JButton("ENCRYPT");
		btnEnkripto.setEnabled(false);
		btnEnkripto.setFocusPainted(false);
		btnEnkripto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnEnkripto.setBackground(btnHover);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEnkripto.setBackground(btnC);
			}
		});
		btnEnkripto.setBackground(btnC);
		btnEnkripto.setIcon(null);
		btnEnkripto.setForeground(new Color(238,238,238));
		btnEnkripto.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnEnkripto.setBounds(45, 194, 407, 49);
		pnUI.add(btnEnkripto);
		btnEnkripto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtKey.getText().length() > 7) {
					lblMessage.setVisible(false);
					btnDekripto.setEnabled(true);
					txtKey.setEditable(false);
					
					try {
						String key = txtKey.getText();
						String fileLocation = txtFile.getText();
						File encryptedFile = new File("C:/Users/PC/Desktop/EncryptedFile.txt");

						FileInputStream fi1 = new FileInputStream(fileLocation);
						FileOutputStream fo1 = new FileOutputStream(encryptedFile);
						encrypt(key, fi1, fo1);

						FileReader ef = new FileReader(encryptedFile);

						Scanner input = new Scanner(ef);
						StringBuilder sb = new StringBuilder();

						while (input.hasNext()) {
							sb.append(input.nextLine());
							sb.append("\n");
						}
						taEncryptedFile.setText(sb.toString());
						input.close();

					} catch (Throwable e) {
						e.printStackTrace();
					}
				} else if(txtKey.getText().length()==0) {
					lblMessage.setVisible(true);
					lblMessage.setText("Enter a key first!");
				}
				else
				{
					lblMessage.setVisible(true);
					lblMessage.setText("Key must be at least 8 characters (64 bit)!");
				}
			}
		});
		
		btnDekripto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtKey.getText().length() > 7) {
					lblMessage.setVisible(false);
					txtKey.setEditable(true);
					
					try {
						String key = txtKey.getText();
						File encryptedFile = new File("C:/Users/PC/Desktop/EncryptedFile.txt");
						File decryptedFile = new File("C:/Users/PC/Desktop/DecryptedFile.txt");

						FileInputStream fi1 = new FileInputStream(encryptedFile);
						FileOutputStream fo1 = new FileOutputStream(decryptedFile);
						decrypt(key, fi1, fo1);

						FileReader df = new FileReader(decryptedFile);

						Scanner input = new Scanner(df);
						StringBuilder sb = new StringBuilder();

						while (input.hasNext()) {
							sb.append(input.nextLine());
							sb.append("\n");
						}
						taDecryptedFile.setText(sb.toString());
						input.close();
					} catch (Throwable p) {
						p.printStackTrace();
					}
				}
			}
		});
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser file = new JFileChooser();

				if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtFile.setText(file.getSelectedFile().toString());
					btnEnkripto.setEnabled(true);
					try {
						Scanner input = new Scanner(file.getSelectedFile());
						StringBuilder sb = new StringBuilder();

						while (input.hasNext()) {
							sb.append(input.nextLine());
							sb.append("\n");
						}
						taOriginalFile.setText(sb.toString());
						input.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					txtFile.setText("File not chosen");
				}
			}
		});
		
		JLabel background = new JLabel("");
		background.setHorizontalAlignment(SwingConstants.CENTER);
		background.setIcon(new ImageIcon(DES_Encryption.class.getResource("/img/background.png")));
		background.setBounds(0, 0, 956, 569);
		pnUI.add(background);
		
		frmDesFileEncryptor.setLocationRelativeTo(null);
	}
}
