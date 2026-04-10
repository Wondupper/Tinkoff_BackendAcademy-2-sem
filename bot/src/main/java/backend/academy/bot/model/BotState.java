package backend.academy.bot.model;

public enum BotState {
    IDLE,
    AWAITING_TRACK_LINK,
    AWAITING_UNTRACK_LINK,
    AWAITING_TAGS,
    AWAITING_FILTERS
}
