package org.javacord.entity.server.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.AccountType;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.Region;
import org.javacord.entity.activity.Activity;
import org.javacord.entity.activity.impl.ImplActivity;
import org.javacord.entity.auditlog.AuditLog;
import org.javacord.entity.auditlog.impl.ImplAuditLog;
import org.javacord.entity.channel.Categorizable;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelBuilder;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.entity.channel.impl.ImplChannelCategory;
import org.javacord.entity.channel.impl.ImplServerTextChannel;
import org.javacord.entity.channel.impl.ImplServerTextChannelBuilder;
import org.javacord.entity.channel.impl.ImplServerVoiceChannel;
import org.javacord.entity.channel.impl.ImplServerVoiceChannelBuilder;
import org.javacord.entity.emoji.CustomEmojiBuilder;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.emoji.impl.ImplCustomEmojiBuilder;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplRole;
import org.javacord.entity.server.Ban;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerUpdater;
import org.javacord.entity.server.VerificationLevel;
import org.javacord.entity.server.invite.RichInvite;
import org.javacord.entity.server.invite.impl.ImplInvite;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.entity.user.impl.ImplUser;
import org.javacord.entity.webhook.Webhook;
import org.javacord.entity.webhook.impl.ImplWebhook;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.listener.channel.server.ServerChannelCreateListener;
import org.javacord.listener.channel.server.ServerChannelDeleteListener;
import org.javacord.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.listener.message.CachedMessagePinListener;
import org.javacord.listener.message.CachedMessageUnpinListener;
import org.javacord.listener.message.ChannelPinsUpdateListener;
import org.javacord.listener.message.MessageCreateListener;
import org.javacord.listener.message.MessageDeleteListener;
import org.javacord.listener.message.MessageEditListener;
import org.javacord.listener.message.reaction.ReactionAddListener;
import org.javacord.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.listener.message.reaction.ReactionRemoveListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.server.ServerBecomesUnavailableListener;
import org.javacord.listener.server.ServerChangeAfkChannelListener;
import org.javacord.listener.server.ServerChangeAfkTimeoutListener;
import org.javacord.listener.server.ServerChangeDefaultMessageNotificationLevelListener;
import org.javacord.listener.server.ServerChangeExplicitContentFilterLevelListener;
import org.javacord.listener.server.ServerChangeIconListener;
import org.javacord.listener.server.ServerChangeMultiFactorAuthenticationLevelListener;
import org.javacord.listener.server.ServerChangeNameListener;
import org.javacord.listener.server.ServerChangeOwnerListener;
import org.javacord.listener.server.ServerChangeRegionListener;
import org.javacord.listener.server.ServerChangeSplashListener;
import org.javacord.listener.server.ServerChangeSystemChannelListener;
import org.javacord.listener.server.ServerChangeVerificationLevelListener;
import org.javacord.listener.server.ServerLeaveListener;
import org.javacord.listener.server.emoji.CustomEmojiChangeNameListener;
import org.javacord.listener.server.emoji.CustomEmojiChangeWhitelistedRolesListener;
import org.javacord.listener.server.emoji.CustomEmojiCreateListener;
import org.javacord.listener.server.emoji.CustomEmojiDeleteListener;
import org.javacord.listener.server.member.ServerMemberBanListener;
import org.javacord.listener.server.member.ServerMemberJoinListener;
import org.javacord.listener.server.member.ServerMemberLeaveListener;
import org.javacord.listener.server.member.ServerMemberUnbanListener;
import org.javacord.listener.server.role.RoleChangeColorListener;
import org.javacord.listener.server.role.RoleChangeHoistListener;
import org.javacord.listener.server.role.RoleChangeMentionableListener;
import org.javacord.listener.server.role.RoleChangeNameListener;
import org.javacord.listener.server.role.RoleChangePermissionsListener;
import org.javacord.listener.server.role.RoleChangePositionListener;
import org.javacord.listener.server.role.RoleCreateListener;
import org.javacord.listener.server.role.RoleDeleteListener;
import org.javacord.listener.server.role.UserRoleAddListener;
import org.javacord.listener.server.role.UserRoleRemoveListener;
import org.javacord.listener.user.UserChangeActivityListener;
import org.javacord.listener.user.UserChangeAvatarListener;
import org.javacord.listener.user.UserChangeNameListener;
import org.javacord.listener.user.UserChangeNicknameListener;
import org.javacord.listener.user.UserChangeStatusListener;
import org.javacord.listener.user.UserStartTypingListener;
import org.javacord.util.ClassHelper;
import org.javacord.util.Cleanupable;
import org.javacord.util.event.ListenerManager;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Server}.
 */
public class ImplServer implements Server, Cleanupable {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplServer.class);

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the server.
     */
    private final long id;

    /**
     * The name of the server.
     */
    private String name;

    /**
     * The region of the server.
     */
    private Region region;

    /**
     * Whether the server is considered as large or not.
     */
    private boolean large;

    /**
     * The id of the owner.
     */
    private long ownerId;

    /**
     * The application id of the owner.
     */
    private long applicationId = -1;

    /**
     * The verification level of the server.
     */
    private VerificationLevel verificationLevel;

    /**
     * The explicit content filter level of the server.
     */
    private ExplicitContentFilterLevel explicitContentFilterLevel;

    /**
     * The default message notification level of the server.
     */
    private DefaultMessageNotificationLevel defaultMessageNotificationLevel;

    /**
     * The multi factor authentication level of the server.
     */
    private MultiFactorAuthenticationLevel multiFactorAuthenticationLevel;

    /**
     * The amount of members in this server.
     */
    private int memberCount;

    /**
     * The icon hash of the server. Might be <code>null</code>.
     */
    private String iconHash;

    /**
     * The splash of the server. Might be <code>null</code>.
     */
    private String splash;

    /**
     * The id of the server's system channel.
     */
    private long systemChannelId = -1;

    /**
     * The id of the server's afk channel.
     */
    private long afkChannelId = -1;

    /**
     * The server's afk timeout.
     */
    private int afkTimeout = 0;

    /**
     * A map with all roles of the server.
     */
    private final ConcurrentHashMap<Long, Role> roles = new ConcurrentHashMap<>();

    /**
     * A map with all channels of the server.
     */
    private final ConcurrentHashMap<Long, ServerChannel> channels = new ConcurrentHashMap<>();

    /**
     * A map with all members of the server.
     */
    private final ConcurrentHashMap<Long, User> members = new ConcurrentHashMap<>();

    /**
     * A map with all nicknames. The key is the user id.
     */
    private final ConcurrentHashMap<Long, String> nicknames = new ConcurrentHashMap<>();

    /**
     * A map with all joinedAt instants. The key is the user id.
     */
    private final ConcurrentHashMap<Long, Instant> joinedAtTimestamps = new ConcurrentHashMap<>();

    /**
     * A list with all custom emojis from this server.
     */
    private final Collection<KnownCustomEmoji> customEmojis = new ArrayList<>();

    /**
     * Creates a new server object.
     *
     * @param api The discord api instance.
     * @param data The json data of the server.
     */
    public ImplServer(ImplDiscordApi api, JsonNode data) {
        this.api = api;

        id = Long.parseLong(data.get("id").asText());
        name = data.get("name").asText();
        region = Region.getRegionByKey(data.get("region").asText());
        large = data.get("large").asBoolean();
        memberCount = data.get("member_count").asInt();
        ownerId = Long.parseLong(data.get("owner_id").asText());
        verificationLevel = VerificationLevel.fromId(data.get("verification_level").asInt());
        explicitContentFilterLevel = ExplicitContentFilterLevel.fromId(data.get("explicit_content_filter").asInt());
        defaultMessageNotificationLevel =
                DefaultMessageNotificationLevel.fromId(data.get("default_message_notifications").asInt());
        multiFactorAuthenticationLevel = MultiFactorAuthenticationLevel.fromId(data.get("mfa_level").asInt());
        if (data.has("icon") && !data.get("icon").isNull()) {
            iconHash = data.get("icon").asText();
        }
        if (data.has("splash") && !data.get("splash").isNull()) {
            splash = data.get("splash").asText();
        }
        if (data.hasNonNull("afk_channel_id")) {
            afkChannelId = data.get("afk_channel_id").asLong();
        }
        if (data.hasNonNull("afk_timeout")) {
            afkTimeout = data.get("afk_timeout").asInt();
        }
        if (data.hasNonNull("system_channel_id")) {
            systemChannelId = data.get("system_channel_id").asLong();
        }
        if (data.hasNonNull("application_id")) {
            applicationId = data.get("application_id").asLong();
        }

        if (data.has("channels")) {
            for (JsonNode channel : data.get("channels")) {
                switch (channel.get("type").asInt()) {
                    case 0:
                        getOrCreateServerTextChannel(channel);
                        break;
                    case 2:
                        getOrCreateServerVoiceChannel(channel);
                        break;
                    case 4:
                        getOrCreateChannelCategory(channel);
                        break;
                }
            }
        }

        if (data.has("roles")) {
            for (JsonNode roleJson : data.get("roles")) {
                Role role = new ImplRole(api, this, roleJson);
                this.roles.put(role.getId(), role);
            }
        }

        if (data.has("members")) {
            addMembers(data.get("members"));
        }

        if (data.hasNonNull("voice_states")) {
            for (JsonNode voiceStateJson : data.get("voice_states")) {
                ImplServerVoiceChannel channel =
                        (ImplServerVoiceChannel) getVoiceChannelById(voiceStateJson.get("channel_id").asLong())
                                .orElseThrow(AssertionError::new);
                ImplUser user = (ImplUser) api.getCachedUserById(voiceStateJson.get("user_id").asLong())
                        .orElseThrow(AssertionError::new);
                channel.addConnectedUser(user);
                user.addConnectedVoiceChannel(channel);
            }
        }

        if ((isLarge() || api.getAccountType() == AccountType.CLIENT) && getMembers().size() < getMemberCount()) {
            this.api.getWebSocketAdapter().queueRequestGuildMembers(this);
        }

        if (data.has("emojis")) {
            for (JsonNode emojiJson : data.get("emojis")) {
                KnownCustomEmoji emoji = api.getOrCreateKnownCustomEmoji(this, emojiJson);
                addCustomEmoji(emoji);
            }
        }

        if (data.has("presences")) {
            for (JsonNode presenceJson : data.get("presences")) {
                long userId = Long.parseLong(presenceJson.get("user").get("id").asText());
                api.getCachedUserById(userId).map(ImplUser.class::cast).ifPresent(user -> {
                    if (presenceJson.has("game")) {
                        Activity activity = null;
                        if (!presenceJson.get("game").isNull()) {
                            activity = new ImplActivity(presenceJson.get("game"));
                        }
                        user.setActivity(activity);
                    }
                    if (presenceJson.has("status")) {
                        UserStatus status = UserStatus.fromString(presenceJson.get("status").asText());
                        user.setStatus(status);
                    }
                });
            }
        }

        api.addServerToCache(this);
    }

    /**
     * Gets the icon hash of the server.
     *
     * @return The icon hash of the server.
     */
    public String getIconHash() {
        return iconHash;
    }

    /**
     * Sets the icon hash of the server.
     *
     * @param iconHash The icon hash of the server.
     */
    public void setIconHash(String iconHash) {
        this.iconHash = iconHash;
    }

    /**
     * Gets the splash hash of the server.
     *
     * @return The splash hash of the server.
     */
    public String getSplashHash() {
        return splash;
    }

    /**
     * Sets the splash hash of the server.
     *
     * @param splashHash The splash hash of the server.
     */
    public void setSplashHash(String splashHash) {
        this.splash = splashHash;
    }

    /**
     * Sets the system channel id of the server.
     *
     * @param systemChannelId The system channel id of the server.
     */
    public void setSystemChannelId(long systemChannelId) {
        this.systemChannelId = systemChannelId;
    }

    /**
     * Sets the afk channel id of the server.
     *
     * @param afkChannelId The afk channel id of the server.
     */
    public void setAfkChannelId(long afkChannelId) {
        this.afkChannelId = afkChannelId;
    }

    /**
     * Sets the afk timeout of the server.
     *
     * @param afkTimeout The afk timeout to set.
     */
    public void setAfkTimeout(int afkTimeout) {
        this.afkTimeout = afkTimeout;
    }

    /**
     * Sets the verification level of the server.
     *
     * @param verificationLevel The verification level of the server.
     */
    public void setVerificationLevel(VerificationLevel verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    /**
     * Sets the region of the server.
     *
     * @param region The region of the server.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Sets the default message notification level of the server.
     *
     * @param defaultMessageNotificationLevel The default message notification level to set.
     */
    public void setDefaultMessageNotificationLevel(DefaultMessageNotificationLevel defaultMessageNotificationLevel) {
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
    }

    /**
     * Sets the server owner id.
     *
     * @param ownerId The owner id to set.
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Sets the application id.
     *
     * @param applicationId The application id to set.
     */
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Sets the explicit content filter level of the server.
     *
     * @param explicitContentFilterLevel The explicit content filter level to set.
     */
    public void setExplicitContentFilterLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
        this.explicitContentFilterLevel = explicitContentFilterLevel;
    }

    /**
     * Sets the multi factor authentication level of the server.
     *
     * @param multiFactorAuthenticationLevel The multi factor authentication level to set.
     */
    public void setMultiFactorAuthenticationLevel(MultiFactorAuthenticationLevel multiFactorAuthenticationLevel) {
        this.multiFactorAuthenticationLevel = multiFactorAuthenticationLevel;
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addChannelToCache(ServerChannel channel) {
        ServerChannel oldChannel = channels.put(channel.getId(), channel);
        if ((oldChannel instanceof Cleanupable) && (oldChannel != channel)) {
            ((Cleanupable) oldChannel).cleanup();
        }
    }

    /**
     * Removes a channel from the cache.
     *
     * @param channelId The id of the channel to remove.
     */
    public void removeChannelFromCache(long channelId) {
        channels.computeIfPresent(channelId, (key, channel) -> {
            if (channel instanceof Cleanupable) {
                ((Cleanupable) channel).cleanup();
            }
            return null;
        });
    }

    /**
     * Removes a role from the cache.
     *
     * @param roleId The id of the role to remove.
     */
    public void removeRole(long roleId) {
        roles.remove(roleId);
    }

    /**
     * Adds a custom emoji.
     *
     * @param emoji The emoji to add.
     */
    public void addCustomEmoji(KnownCustomEmoji emoji) {
        customEmojis.add(emoji);
    }

    /**
     * Removes a custom emoji.
     *
     * @param emoji The emoji to remove.
     */
    public void removeCustomEmoji(KnownCustomEmoji emoji) {
        customEmojis.remove(emoji);
    }

    /**
     * Gets or create a new role.
     *
     * @param data The json data of the role.
     * @return The role.
     */
    public Role getOrCreateRole(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        synchronized (this) {
            return getRoleById(id).orElseGet(() -> {
                Role role = new ImplRole(api, this, data);
                this.roles.put(role.getId(), role);
                return role;
            });
        }
    }

    /**
     * Gets or creates a channel category.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ChannelCategory getOrCreateChannelCategory(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 4) {
                return getChannelCategoryById(id).orElseGet(() -> new ImplChannelCategory(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server text channel.
     *
     * @param data The json data of the channel.
     * @return The server text channel.
     */
    public ServerTextChannel getOrCreateServerTextChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 0) {
                return getTextChannelById(id).orElseGet(() -> new ImplServerTextChannel(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Gets or creates a server voice channel.
     *
     * @param data The json data of the channel.
     * @return The server voice channel.
     */
    public ServerVoiceChannel getOrCreateServerVoiceChannel(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        int type = data.get("type").asInt();
        synchronized (this) {
            if (type == 2) {
                return getVoiceChannelById(id).orElseGet(() -> new ImplServerVoiceChannel(api, this, data));
            }
        }
        // Invalid channel type
        return null;
    }

    /**
     * Removes a member from the server.
     *
     * @param user The user to remove.
     */
    public void removeMember(User user) {
        members.remove(user.getId());
        nicknames.remove(user.getId());
        getRoles().forEach(role -> ((ImplRole) role).removeUserFromCache(user));
        joinedAtTimestamps.remove(user.getId());
    }

    /**
     * Decrements the member count.
     */
    public void decrementMemberCount() {
        memberCount--;
    }

    /**
     * Adds a member to the server.
     *
     * @param member The user to add.
     */
    public void addMember(JsonNode member) {
        User user = api.getOrCreateUser(member.get("user"));
        members.put(user.getId(), user);
        if (member.has("nick") && !member.get("nick").isNull()) {
            nicknames.put(user.getId(), member.get("nick").asText());
        }

        for (JsonNode roleIds : member.get("roles")) {
            long roleId = Long.parseLong(roleIds.asText());
            getRoleById(roleId).map(role -> ((ImplRole) role)).ifPresent(role -> role.addUserToCache(user));
        }

        joinedAtTimestamps.put(user.getId(), OffsetDateTime.parse(member.get("joined_at").asText()).toInstant());
    }

    /**
     * Increments the member count.
     */
    public void incrementMemberCount() {
        memberCount++;
    }

    /**
     * Sets the nickname of the user.
     *
     * @param user The user.
     * @param nickname The nickname to set.
     */
    public void setNickname(User user, String nickname) {
        if (nickname == null) {
            nicknames.remove(user.getId());
        } else {
            nicknames.put(user.getId(), nickname);
        }
    }

    /**
     * Adds members to the server.
     *
     * @param members An array of guild member objects.
     */
    public void addMembers(JsonNode members) {
        for (JsonNode member : members) {
            addMember(member);
        }
    }

    /**
     * Sets the name of the server.
     *
     * @param name The name of the server.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets an unordered collection with all channels in the server.
     *
     * @return An unordered collection with all channels in the server.
     */
    public Collection<ServerChannel> getUnorderedChannels() {
        return channels.values();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public Optional<String> getNickname(User user) {
        return Optional.ofNullable(nicknames.get(user.getId()));
    }

    @Override
    public Optional<Instant> getJoinedAtTimestamp(User user) {
        return Optional.ofNullable(joinedAtTimestamps.get(user.getId()));
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public User getOwner() {
        return api.getCachedUserById(ownerId)
                .orElseThrow(() -> new IllegalStateException("Owner of server " + toString() + " is not cached!"));
    }

    @Override
    public Optional<Long> getApplicationId() {
        return applicationId != -1 ? Optional.of(applicationId) : Optional.empty();
    }

    @Override
    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    @Override
    public ExplicitContentFilterLevel getExplicitContentFilterLevel() {
        return explicitContentFilterLevel;
    }

    @Override
    public DefaultMessageNotificationLevel getDefaultMessageNotificationLevel() {
        return defaultMessageNotificationLevel;
    }

    @Override
    public MultiFactorAuthenticationLevel getMultiFactorAuthenticationLevel() {
        return multiFactorAuthenticationLevel;
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(),
                    new URL("https://cdn.discordapp.com/icons/" + getIdAsString() + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Icon> getSplash() {
        if (splash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(),
                    new URL("https://cdn.discordapp.com/splashes/" + getIdAsString() + "/" + splash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ServerTextChannel> getSystemChannel() {
        return getTextChannelById(systemChannelId);
    }

    @Override
    public Optional<ServerVoiceChannel> getAfkChannel() {
        return getVoiceChannelById(afkChannelId);
    }

    @Override
    public int getAfkTimeoutInSeconds() {
        return afkTimeout;
    }

    @Override
    public CompletableFuture<Integer> getPruneCount(int days) {
        return new RestRequest<Integer>(getApi(), RestMethod.GET, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

    @Override
    public CompletableFuture<Integer> pruneMembers(int days, String reason) {
        return new RestRequest<Integer>(getApi(), RestMethod.POST, RestEndpoint.SERVER_PRUNE)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("days", String.valueOf(days))
                .setAuditLogReason(reason)
                .execute(result -> result.getJsonBody().get("pruned").asInt());
    }

    @Override
    public CompletableFuture<Collection<RichInvite>> getInvites() {
        return new RestRequest<Collection<RichInvite>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_INVITE)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<RichInvite> invites = new HashSet<>();
                    for (JsonNode inviteJson : result.getJsonBody()) {
                        invites.add(new ImplInvite(getApi(), inviteJson));
                    }
                    return Collections.unmodifiableCollection(invites);
                });
    }

    @Override
    public Collection<User> getMembers() {
        return Collections.unmodifiableList(new ArrayList<>(members.values()));
    }

    @Override
    public Optional<User> getMemberById(long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles.values().stream()
                .sorted(Comparator.comparingInt(Role::getPosition))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return Optional.ofNullable(roles.get(id));
    }

    @Override
    public CustomEmojiBuilder createCustomEmojiBuilder() {
        return new ImplCustomEmojiBuilder(this);
    }

    @Override
    public ServerUpdater createUpdater() {
        return new ImplServerUpdater(this);
    }

    @Override
    public CompletableFuture<Void> updateNickname(User user, String nickname, String reason) {
        if (user.isYourself()) {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.OWN_NICKNAME)
                    .setUrlParameters(getIdAsString())
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                    .setUrlParameters(getIdAsString(), user.getIdAsString())
                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        }
    }

    @Override
    public CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> leave() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_SELF)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> updateRoles(User user, Collection<Role> roles, String reason) {
        ObjectNode updateNode = JsonNodeFactory.instance.objectNode();
        ArrayNode rolesJson = updateNode.putArray("roles");
        for (Role role : roles) {
            rolesJson.add(role.getIdAsString());
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setBody(updateNode)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> reorderRoles(List<Role> roles, String reason) {
        roles = new ArrayList<>(roles); // Copy the list to safely modify it
        ArrayNode body = JsonNodeFactory.instance.arrayNode();
        roles.removeIf(Role::isEveryoneRole);
        for (int i = 0; i < roles.size(); i++) {
            body.addObject()
                    .put("id", roles.get(i).getIdAsString())
                    .put("position", i + 1);
        }
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                .setUrlParameters(getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> kickUser(User user, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.SERVER_MEMBER)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> banUser(User user, int deleteMessageDays, String reason) {
        RestRequest<Void> request = new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), user.getIdAsString())
                .addQueryParameter("delete-message-days", String.valueOf(deleteMessageDays));
        if (reason != null) {
            request.addHeader("reason", reason);
        }
        return request.execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> unbanUser(long userId, String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString(), Long.toUnsignedString(userId))
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Collection<Ban>> getBans() {
        return new RestRequest<Collection<Ban>>(getApi(), RestMethod.GET, RestEndpoint.BAN)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<Ban> bans = new ArrayList<>();
                    for (JsonNode ban : result.getJsonBody()) {
                        bans.add(new ImplBan(this, ban));
                    }
                    return Collections.unmodifiableCollection(bans);
                });
    }

    @Override
    public CompletableFuture<List<Webhook>> getWebhooks() {
        return new RestRequest<List<Webhook>>(getApi(), RestMethod.GET, RestEndpoint.SERVER_WEBHOOK)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    List<Webhook> webhooks = new ArrayList<>();
                    for (JsonNode webhookJson : result.getJsonBody()) {
                        webhooks.add(new ImplWebhook(getApi(), webhookJson));
                    }
                    return Collections.unmodifiableList(webhooks);
                });
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLog(int limit) {
        return new RestRequest<AuditLog>(getApi(), RestMethod.GET, RestEndpoint.AUDIT_LOG)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("limit", String.valueOf(limit))
                .execute(result -> new ImplAuditLog(getApi(), result.getJsonBody()));
    }

    @Override
    public Collection<KnownCustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(new ArrayList<>(customEmojis));
    }

    @Override
    public ServerTextChannelBuilder createTextChannelBuilder() {
        return new ImplServerTextChannelBuilder(this);
    }

    @Override
    public ServerVoiceChannelBuilder createVoiceChannelBuilder() {
        return new ImplServerVoiceChannelBuilder(this);
    }

    @Override
    public List<ServerChannel> getChannels() {
        Collection<ServerChannel> channelsUnordered = this.channels.values();
        List<ServerChannel> channels = new ArrayList<>();
        channelsUnordered.stream()
                .filter(channel -> !channel.asChannelCategory().isPresent())
                .filter(channel -> channel.asServerTextChannel().isPresent())
                .filter(channel -> !channel.asServerTextChannel().flatMap(Categorizable::getCategory).isPresent())
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEachOrdered(channels::add);
        channelsUnordered.stream()
                .filter(channel -> !channel.asChannelCategory().isPresent())
                .filter(channel -> channel.asServerVoiceChannel().isPresent())
                .filter(channel -> !channel.asServerVoiceChannel().flatMap(Categorizable::getCategory).isPresent())
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .forEachOrdered(channels::add);
        getChannelCategories().forEach(category -> {
            channels.add(category);
            channels.addAll(category.getChannels());
        });
        return Collections.unmodifiableList(channels);
    }

    @Override
    public List<ChannelCategory> getChannelCategories() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ChannelCategory)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ChannelCategory) channel)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerTextChannel> getTextChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ServerTextChannel)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ServerTextChannel) channel)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ServerVoiceChannel> getVoiceChannels() {
        return Collections.unmodifiableList(getUnorderedChannels().stream()
                .filter(channel -> channel instanceof ServerVoiceChannel)
                .sorted(Comparator.comparingInt(ServerChannel::getRawPosition))
                .map(channel -> (ServerVoiceChannel) channel)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public void cleanup() {
        channels.values().stream()
                .filter(Cleanupable.class::isInstance)
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageCreateListener.class);
    }

    @Override
    public ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerLeaveListener.class, listener);
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerBecomesUnavailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerBecomesUnavailableListener.class);
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserStartTypingListener.class);
    }

    @Override
    public ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(
            ServerChannelCreateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelCreateListener.class, listener);
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChannelCreateListener.class);
    }

    @Override
    public ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelDeleteListener.class, listener);
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageDeleteListener.class, listener);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), MessageEditListener.class, listener);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), MessageEditListener.class);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionAddListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionRemoveListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(
            ReactionRemoveAllListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ReactionRemoveAllListener.class, listener);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ReactionRemoveAllListener.class);
    }

    @Override
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberBanListener.class, listener);
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberBanListener.class);
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerMemberUnbanListener.class, listener);
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerMemberUnbanListener.class);
    }

    @Override
    public ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChangeNameListener> getServerChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeIconListener.class, listener);
    }

    @Override
    public List<ServerChangeIconListener> getServerChangeIconListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeIconListener.class);
    }

    @Override
    public ListenerManager<ServerChangeSplashListener> addServerChangeSplashListener(ServerChangeSplashListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeSplashListener.class, listener);
    }

    @Override
    public List<ServerChangeSplashListener> getServerChangeSplashListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeSplashListener.class);
    }

    @Override
    public ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeVerificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeVerificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(
            ServerChangeRegionListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeRegionListener.class, listener);
    }

    @Override
    public List<ServerChangeRegionListener> getServerChangeRegionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeRegionListener.class);
    }

    @Override
    public ListenerManager<ServerChangeDefaultMessageNotificationLevelListener>
    addServerChangeDefaultMessageNotificationLevelListener(
            ServerChangeDefaultMessageNotificationLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeDefaultMessageNotificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeDefaultMessageNotificationLevelListener>
    getServerChangeDefaultMessageNotificationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeDefaultMessageNotificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(
            ServerChangeOwnerListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeOwnerListener.class, listener);
    }

    @Override
    public List<ServerChangeOwnerListener> getServerChangeOwnerListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), ServerChangeOwnerListener.class);
    }

    @Override
    public ListenerManager<ServerChangeExplicitContentFilterLevelListener>
    addServerChangeExplicitContentFilterLevelListener(ServerChangeExplicitContentFilterLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeExplicitContentFilterLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeExplicitContentFilterLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
    addServerChangeMultiFactorAuthenticationLevelListener(ServerChangeMultiFactorAuthenticationLevelListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChangeMultiFactorAuthenticationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeMultiFactorAuthenticationLevelListener>
    getServerChangeMultiFactorAuthenticationLevelListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerChangeMultiFactorAuthenticationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeSystemChannelListener> addServerChangeSystemChannelListener(
            ServerChangeSystemChannelListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeSystemChannelListener.class, listener);
    }

    @Override
    public List<ServerChangeSystemChannelListener> getServerChangeSystemChannelListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChangeSystemChannelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeAfkChannelListener> addServerChangeAfkChannelListener(
            ServerChangeAfkChannelListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeAfkChannelListener.class, listener);
    }

    @Override
    public List<ServerChangeAfkChannelListener> getServerChangeAfkChannelListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChangeAfkChannelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeAfkTimeoutListener> addServerChangeAfkTimeoutListener(
            ServerChangeAfkTimeoutListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerChangeAfkTimeoutListener.class, listener);
    }

    @Override
    public List<ServerChangeAfkTimeoutListener> getServerChangeAfkTimeoutListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChangeAfkTimeoutListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeNsfwFlagListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeNsfwFlagListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangePositionListener.class, listener);
    }

    @Override
    public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangePositionListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiCreateListener> addCustomEmojiCreateListener(
            CustomEmojiCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiCreateListener.class, listener);
    }

    @Override
    public List<CustomEmojiCreateListener> getCustomEmojiCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CustomEmojiCreateListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiChangeNameListener.class, listener);
    }

    @Override
    public List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), CustomEmojiChangeNameListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiChangeWhitelistedRolesListener> addCustomEmojiChangeWhitelistedRolesListener(
            CustomEmojiChangeWhitelistedRolesListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class, listener);
    }

    @Override
    public List<CustomEmojiChangeWhitelistedRolesListener> getCustomEmojiChangeWhitelistedRolesListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(
            CustomEmojiDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CustomEmojiDeleteListener.class, listener);
    }

    @Override
    public List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CustomEmojiDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeActivityListener.class, listener);
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeActivityListener.class);
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeStatusListener.class, listener);
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeStatusListener.class);
    }

    @Override
    public ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeColorListener.class, listener);
    }

    @Override
    public List<RoleChangeColorListener> getRoleChangeColorListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeColorListener.class);
    }

    @Override
    public ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeHoistListener.class, listener);
    }

    @Override
    public List<RoleChangeHoistListener> getRoleChangeHoistListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeHoistListener.class);
    }

    @Override
    public ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeMentionableListener.class, listener);
    }

    @Override
    public List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), RoleChangeMentionableListener.class);
    }

    @Override
    public ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangeNameListener.class, listener);
    }

    @Override
    public List<RoleChangeNameListener> getRoleChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangeNameListener.class);
    }

    @Override
    public ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangePermissionsListener.class, listener);
    }

    @Override
    public List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), RoleChangePermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), RoleChangePositionListener.class, listener);
    }

    @Override
    public List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
    getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), RoleCreateListener.class, listener);
    }

    @Override
    public List<RoleCreateListener> getRoleCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleCreateListener.class);
    }

    @Override
    public ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(Server.class, getId(), RoleDeleteListener.class, listener);
    }

    @Override
    public List<RoleDeleteListener> getRoleDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), RoleDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), UserChangeNicknameListener.class, listener);
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeNicknameListener.class);
    }

    @Override
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Server.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserRoleAddListener.class, listener);
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserRoleRemoveListener.class, listener);
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserRoleRemoveListener.class);
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeNameListener.class, listener);
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeNameListener.class);
    }

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), UserChangeAvatarListener.class, listener);
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), UserChangeAvatarListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelChangeBitrateListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ServerVoiceChannelChangeUserLimitListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Server.class, getId(), ServerVoiceChannelChangeUserLimitListener.class);
    }

    @Override
    public ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), WebhooksUpdateListener.class, listener);
    }

    @Override
    public List<WebhooksUpdateListener> getWebhooksUpdateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), WebhooksUpdateListener.class);
    }

    @Override
    public ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(
            ChannelPinsUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), ChannelPinsUpdateListener.class, listener);
    }

    @Override
    public List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(Server.class, getId(), ChannelPinsUpdateListener.class);
    }

    @Override
    public ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CachedMessagePinListener.class, listener);
    }

    @Override
    public List<CachedMessagePinListener> getCachedMessagePinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CachedMessagePinListener.class);
    }

    @Override
    public ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(CachedMessageUnpinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Server.class, getId(), CachedMessageUnpinListener.class, listener);
    }

    @Override
    public List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId(), CachedMessageUnpinListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addServerAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Server.class, getId(),
                        listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerAttachableListener & ObjectAttachableListener> void removeServerAttachableListener(
            T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Server.class, getId(),
                        listenerClass, listener));
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Server.class, getId());
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Server.class, getId(), listenerClass, listener);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Server (id: %s, name: %s)", getIdAsString(), getName());
    }

}
