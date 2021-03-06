/**
 *  Passman Android App
 *
 * @copyright Copyright (c) 2016, Sander Brand (brantje@gmail.com)
 * @copyright Copyright (c) 2016, Marcos Zuriaga Miguel (wolfi@wolfi.es)
 * @license GNU AGPL version 3 or any later version
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package es.wolfi.passman.API;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Credential extends Core {
    public int id;



    protected String guid;
    protected int vaultId;
    protected String userId;
    protected String label;
    protected String description;
    protected long created;
    protected long changed;
    protected String tags;
    protected String email;
    protected String username;
    protected String password;
    protected String url;
    protected String favicon;
    protected long renewInterval;
    protected long expireTime;
    protected long deleteTime;
    protected String files;
    protected String customFields;
    protected String otp;
    protected boolean hidden;
    protected String sharedKey;

    protected Vault vault;

    public int getId() {
        return id;
    }

    public String getGuid() {
        return guid;
    }

    public int getVaultId() {
        return vaultId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return vault.decryptString(description);
    }

    public void setDescription(String description) {
        this.description = vault.encryptString(description);
    }

    public long getCreated() {
        return created;
    }

    public long getChanged() {
        return changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }

    public String getTags() {
        return vault.decryptString(tags);
    }

    public void setTags(String tags) {
        this.tags = vault.encryptString(tags);
    }

    public String getEmail() {
        return vault.decryptString(email);
    }

    public void setEmail(String email) {
        this.email = vault.encryptString(email);
    }

    public String getUsername() {
        return vault.decryptString(username);
    }

    public void setUsername(String username) {
        this.username = vault.encryptString(username);
    }

    public String getPassword() {
        return vault.decryptString(password);
    }

    public void setPassword(String password) {
        this.password = vault.encryptString(password);
    }

    public String getUrl() {
        return vault.decryptString(url);
    }

    public void setUrl(String url) {
        this.url = vault.encryptString(url);
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public long getRenewInterval() {
        return renewInterval;
    }

    public void setRenewInterval(long renewInterval) {
        this.renewInterval = renewInterval;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getFiles() {
        return vault.decryptString(files);
    }

    public void setFiles(String files) {
        this.files = vault.encryptString(files);
    }

    public String getCustomFields() {
        return vault.decryptString(customFields);
    }

    public void setCustomFields(String customFields) {
        this.customFields = vault.encryptString(customFields);
    }

    public String getOtp() {
        return vault.decryptString(otp);
    }

    public void setOtp(String otp) {
        this.otp = vault.encryptString(otp);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public Vault getVault() {
        return vault;
    }

    public void setVault(Vault v) {
        vault = v;
    }

    public static Credential fromJSON(JSONObject j) throws JSONException {
        Credential c = new Credential();

        c.id = j.getInt("credential_id");
        c.guid = j.getString("guid");
        c.vaultId = j.getInt("vault_id");
        c.userId = j.getString("user_id");
        c.label = j.getString("label");
        c.description = j.getString("description");
        c.created = j.getLong("created");
        c.changed = j.getLong("changed");
        c.tags = j.getString("tags");
        c.email = j.getString("email");
        c.username = j.getString("username");
        c.password = j.getString("password");
        c.url = j.getString("url");
        c.favicon = j.getString("favicon");

        if (j.isNull("renew_interval")) {
            c.renewInterval = 0;
        }
        else {
            c.renewInterval = j.getLong("renew_interval");
        }

        c.expireTime = j.getLong("expire_time");
        c.deleteTime = j.getLong("delete_time");
        c.files = j.getString("files");
        c.customFields = j.getString("custom_fields");
        c.otp = j.getString("otp");
        c.hidden = (j.getInt("hidden") > 0);
        c.sharedKey = j.getString("shared_key");

        return c;
    }

    public static Credential fromJSON(JSONObject j, Vault v) throws JSONException {
        Credential c = Credential.fromJSON(j);
        c.setVault(v);
        return c;
    }
}
