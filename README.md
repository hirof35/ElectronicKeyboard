Java MIDI Visualizer KeyboardJava SwingとMIDIシステムを融合させた、インタラクティブなビジュアル・キーボード・アプリケーションです。キーボード入力やマウス操作に連動して、リアルタイムに生成されるパーティクルエフェクトと共に高品質な楽器音を楽しむことができます。
<img width="981" height="730" alt="スクリーンショット 2026-05-23 191554" src="https://github.com/user-attachments/assets/9f9f56ce-0e9e-407e-94b4-e9d972f4e7fe" />
✨ 主な特徴リアルタイム・ビジュアライザー:演奏に同期して鮮やかなパーティクルが画面上を舞います。
ピアノ音色は上昇し、打楽器系は重力に従うなど、楽器ごとに物理挙動が変化します。
多様な内蔵音色:ピアノ、オルガン、ストリングス、フルートに加え、スティールドラムやGunshot（効果音）を切り替えて演奏可能。
デュアル操作モード:PCキーボード: A S D F G H J K キーがドレミファソラシドに対応。マウス操作: 画面をクリックすることで、直感的に音を奏でることも可能です。
特殊演出:SFX音色選択時には、画面全体がフラッシュする専用の視覚効果を実装。🕹 操作方法キー音階 / アクションAド (C4)Sレ (D4)Dミ (E4)Fファ (F4)Gソ (G4)Hラ (A4)Jシ (B4)Kド (C5)Instrument Menu楽器の切り替え
🛠 技術構成Language: Java 17+Framework: Java Swing (GUI)API:javax.sound.midi: MIDIシンセサイザーの制御Graphics2D: パーティクルのアンチエイリアス・半透明描画Concurrency: CopyOnWriteArrayList を用いたスレッドセーフなパーティクル管理🚀 実行方法本プロジェクトは外部ライブラリに依存していないため、標準的なJava環境ですぐに実行可能です。
リポジトリをクローンまたはダウンロードします。
ElectronicKeyboard.java をコンパイルして実行します。
Bash# コンパイル
javac electronicKeyboard/ElectronicKeyboard.java

# 実行
java electronicKeyboard.ElectronicKeyboard
📝 ライセンスこのプロジェクトは MITライセンス の下で公開されています。
Created with the harmony of sound and vision.
💡 今後のロードマップ[ ] 録音および再生機能の追加[ ] 外部MIDIデバイスからの入力サポート[ ] パーティクルスタイルのカスタマイズ設定



