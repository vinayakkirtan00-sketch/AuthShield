</head>
<body>

  <h1>🛡️ AuthShield – Secure Player Authentication for Minecraft: Bedrock & Java Servers</h1>

  <p><b>AuthShield</b> is a powerful authentication and protection plugin designed to safeguard your Minecraft server from unauthorized access and session hijacking.  
  It ensures that only verified players can join and play — keeping your world secure and your community safe.</p>

  <hr>

  <div class="section">
    <h2>🚀 Features</h2>
    <ul>
      <li>Secure password-based login system</li>
      <li>Automatic registration prompt for new players</li>
      <li>Configurable login timeout and kick messages</li>
      <li>Supports both Bedrock and Java (Spigot) environments</li>
      <li>Multi-language support</li>
      <li>Lightweight and lag-free — perfect for all server sizes</li>
      <li>Admin commands for player management</li>
    </ul>
  </div>

  <hr>

  <div class="section">
    <h2>⚙️ Commands & Permissions</h2>
    <table>
      <tr>
        <th>Command</th>
        <th>Description</th>
        <th>Permission</th>
      </tr>
      <tr>
        <td>/register &lt;password&gt; &lt;confirm&gt;</td>
        <td>Register a new account</td>
        <td>—</td>
      </tr>
      <tr>
        <td>/login &lt;password&gt;</td>
        <td>Log into your account</td>
        <td>—</td>
      </tr>
      <tr>
        <td>/changepass &lt;old&gt; &lt;new&gt;</td>
        <td>Change your password</td>
        <td>authshield.changepass</td>
      </tr>
      <tr>
        <td>/unregister &lt;player&gt;</td>
        <td>Remove a player’s registration</td>
        <td>authshield.admin</td>
      </tr>
      <tr>
        <td>/authreload</td>
        <td>Reload configuration</td>
        <td>authshield.reload</td>
      </tr>
    </table>
  </div>

  <hr>

  <div class="section">
    <h2>🧩 Configuration</h2>
    <p>Easily customize all settings in the <code>config.yml</code>:</p>
    <table>
      <tr>
        <th>Setting</th>
        <th>Description</th>
        <th>Default Value</th>
      </tr>
      <tr>
        <td>login-timeout</td>
        <td>Time (in seconds) allowed to log in before being kicked</td>
        <td>60</td>
      </tr>
      <tr>
        <td>max-login-attempts</td>
        <td>Number of failed login attempts allowed</td>
        <td>3</td>
      </tr>
      <tr>
        <td>kick-message</td>
        <td>Message displayed when login fails</td>
        <td><code>§cLogin failed! Please try again.</code></td>
      </tr>
    </table>
  </div>

  <hr>

  <div class="section">
    <h2>📦 Download</h2>
    <p>➡️ <a href="https://www.spigotmc.org/resources/authshield.129253/" target="_blank">Download AuthShield from SpigotMC</a></p>
  </div>

  <hr>

  <div class="section">
    <h2>📁 Installation</h2>
    <ol>
      <li>Download the latest version from the link above.</li>
      <li>Place the <code>.jar</code> file into your server’s <code>plugins/</code> folder.</li>
      <li>Restart the server.</li>
      <li>Configure settings in <code>config.yml</code> to your preference.</li>
      <li>Enjoy a secure and smooth authentication system!</li>
    </ol>
  </div>

  <hr>

  <div class="section">
    <h2>🧑‍💻 Developer Info</h2>
    <table>
      <tr>
        <th>Field</th>
        <th>Detail</th>
      </tr>
      <tr>
        <td>Author</td>
        <td>Kirtan Vinayak</td>
      </tr>
      <tr>
        <td>Version</td>
        <td>1.0.0</td>
      </tr>
      <tr>
        <td>Supported Platforms</td>
        <td>Minecraft Bedrock (PMMP API 5+) &amp; Java (Spigot 1.21+)</td>
      </tr>
    </table>
  </div>

</body>
</html>
