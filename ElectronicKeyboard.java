package electronicKeyboard;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ElectronicKeyboard extends JFrame {
    private Synthesizer synth;
    private MidiChannel channel;
    private VisualizerPanel visualizerPanel;
    private int currentPatch = 0; // 0: Piano

    public ElectronicKeyboard() {
        setTitle("Java MIDI Visualizer Keyboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        try {
            // 1. MIDIシステムの初期化
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channel = synth.getChannels()[0];
            
            // 2. GUIのセットアップ
            visualizerPanel = new VisualizerPanel();
            setLayout(new BorderLayout());
            add(visualizerPanel, BorderLayout.CENTER);
            
            // コントロールパネル（楽器切り替え）
            JPanel controls = new JPanel();
            String[] instruments = {"Piano", "Church Organ", "Strings", "Flute", "Steel Drum", "Gunshot (SFX)"};
            int[] patches = {0, 19, 48, 73, 114, 127};
            JComboBox<String> combo = new JComboBox<>(instruments);
            combo.addActionListener(e -> {
                currentPatch = patches[combo.getSelectedIndex()];
                channel.programChange(currentPatch);
            });
            controls.add(new JLabel("Instrument: "));
            controls.add(combo);
            add(controls, BorderLayout.SOUTH);

            // 3. キーボード入力設定 (A,S,D,F,G,H,J,K = ドレミファソラシド)
            setupKeyBindings();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyBindings() {
        String keys = "ASDFGHJK";
        int[] notes = {60, 62, 64, 65, 67, 69, 71, 72}; // MIDI note numbers

        visualizerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playNote(60 + (e.getX() / 50), 100, e.getX(), e.getY());
            }
        });

        // キーボード入力を受け付けるためにフォーカスを当てる
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int idx = keys.indexOf(Character.toUpperCase(e.getKeyChar()));
                if (idx != -1) {
                    // 画面中央付近にエフェクトを出す
                    playNote(notes[idx], 100, 100 + (idx * 80), 300);
                }
            }
        });
    }

    private void playNote(int note, int velocity, int x, int y) {
        channel.noteOn(note, velocity);
        // エフェクトマネージャーにエフェクトを追加
        visualizerPanel.spawnEffect(x, y, velocity, currentPatch);
    }

    // --- インナークラス: ビジュアライザーパネル ---
    class VisualizerPanel extends JPanel {
        private List<Particle> particles = new CopyOnWriteArrayList<>();
        private float flashAlpha = 0f;

        public VisualizerPanel() {
            setBackground(Color.BLACK);
            // 描画ループ (約60FPS)
            Timer timer = new Timer(16, e -> {
                updateParticles();
                repaint();
            });
            timer.start();
        }

        public void spawnEffect(int x, int y, int velocity, int patch) {
            if (patch == 127) flashAlpha = 0.8f; // SFXならフラッシュ

            int count = (int) (velocity / 127.0 * 20);
            Color color = Color.getHSBColor((float)Math.random(), 0.8f, 1.0f);
            
            for (int i = 0; i < count; i++) {
                particles.add(new Particle(x, y, color, patch));
            }
        }

        private void updateParticles() {
            for (Particle p : particles) {
                p.update();
                if (p.life <= 0) particles.remove(p);
            }
            if (flashAlpha > 0) flashAlpha -= 0.05f;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 粒子の描画
            for (Particle p : particles) {
                p.draw(g2d);
            }

            // フラッシュ/反転演出
            if (flashAlpha > 0) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, flashAlpha));
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // --- インナークラス: パーティクル ---
    class Particle {
        double x, y, vx, vy;
        int life = 255;
        Color color;
        int patch;

        public Particle(int x, int y, Color color, int patch) {
            this.x = x; this.y = y; this.color = color; this.patch = patch;
            this.vx = (Math.random() - 0.5) * 8;
            this.vy = (Math.random() - 0.5) * 8;
        }

        public void update() {
            x += vx; y += vy;
            if (patch == 0) vy -= 0.1; // ピアノなら上昇
            else vy += 0.2;            // 他は重力
            life -= 4;
        }

        public void draw(Graphics2D g2d) {
            int alpha = Math.max(0, life);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            
            if (patch == 0) { // ピアノは四角
                g2d.fillRect((int)x, (int)y, 8, 8);
            } else { // 他は丸
                g2d.fillOval((int)x, (int)y, 10, 10);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectronicKeyboard().setVisible(true));
    }
}
