package org.drama.text;

import static org.drama.text.Symbol.LAYER;
import static org.drama.text.Symbol.STAGE;

import java.text.MessageFormat;

public final class MessageText {
	public static final String STAGE_DEAL = STAGE + " addressed event<{0}>";
    public static final String STAGE_RECEVIE = STAGE + " recevied event'{' {0} '}'";
    public static final String LAYER_BROADCAST = LAYER + " Layer<{0}> broadcast event<{1}>";
    public static final String LAYER_HANDING = LAYER + " handing element<{0}>";
    public static final String STAGE_IS_RUNNING = STAGE + " Stage<{0}> is running!";
    public static final String REGISTERED_LAYER = STAGE + " registered Layer<{0}>";
    public static final String REGISTERED_ELEMENT = STAGE + " registered element<{0}>";
    public static final String REGISTERED_EVENT = STAGE + " registered event<{0}>";
    
    public static final String EMPTY_EVENT_MSG = "Without any event registered";
    public static final String EMPTY_ELEMENS_MSG = "Without any elemen registered";
    public static final String PLAYE_RROR_MSG = "Occurred errer on play";
    public static final String ILLEGAL_EVENT_MSG = "Illegal event<{0}>. maybe unregister or unkonw event name, please check event register";
    public static final String ILLEGAL_BROADCAST_MSG = "Illegal broadcast event on layer<{0}>";
    public static final String ELEM_HANDING_ERROR = "Element<{0}> occurring error on handing.";
    public static final String NO_SPECIAL_LAYER_PROP_MSG = "Layer<{0}> must special a LayerProperty annotaioin";
    public static final String ILLEGAL_LAYER_DESC = "Illegal layer special target [{0}] to enum [{1}] in LayerDescription annotation";
    public static final String ILLEGAL_LAYER_DESCRIPTOR = "Illegal layer descripter[{0}] with name [{1}] not found";
    
    public static String format(String template, Object... objects) {
    	return MessageFormat.format(template, objects);
    }
}
