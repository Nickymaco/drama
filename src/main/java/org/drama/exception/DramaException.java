package org.drama.exception;

import org.drama.annotation.LayerDescription;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.core.LayerDescriptor;
import org.drama.event.Event;

import java.util.Objects;

public class DramaException extends RuntimeException {
    static final String EMPTY_EVENT_MSG = "Without any event registered";
    static final String EMPTY_ELEMENS_MSG = "Without any elemen registered";
    static final String PLAYE_RROR_MSG = "Occurred errer on play";
    static final String ILLEGAL_EVENT_MSG = "Illegal event<%s>. maybe unregister or unkonw event name, please check event register";
    static final String ILLEGAL_BROADCAST_MSG = "Illegal broadcast event on layer<%s>";
    static final String ELEM_HANDING_ERROR = "Element<%s> occurring error on handing.";
    static final String ONLY_GLOBALE_EVENT_MSG = "Element<%s> register more event, Global Event only. don't need any other event";
    static final String NO_SPECIAL_LAYER_PROP_MSG = "Layer<%s> must special a LayerProperty annotaioin";
    static final String ILLEGAL_LAYER_DESC = "Illegal layer special target [%s] to enum [%s] in LayerDescription annotation";
    static final String ILLEGAL_LAYER_DESCRIPTOR = "Illegal layer descripter[%s] with name [%s] not found";

    private static final long serialVersionUID = -7337990653787626209L;

    public DramaException(String message) {
        super(message);
    }

    public DramaException(Throwable e, String message) {
        super(message, e);
    }


    public static DramaException emptyRegisterEvents() {
        return new DramaException(EMPTY_EVENT_MSG);
    }

    public static DramaException emptyRegisterElements() {
        return new DramaException(EMPTY_ELEMENS_MSG);
    }

    public static DramaException occurredPlayError(Throwable e, Event event) {
        return new DramaException(e, PLAYE_RROR_MSG);
    }

    public static DramaException illegalRegisterEvent(Event event) {
        String message = String.format(ILLEGAL_EVENT_MSG, event.getClass().getName());
        return new DramaException(message);
    }

    public static DramaException illegalBroadcastEvent(Layer layer, Event event) {
        String layerName = layer.getClass().getSimpleName();
        String msg = String.format(ILLEGAL_BROADCAST_MSG, layerName);

        return Objects.isNull(event) ? new DramaException(new NullPointerException(), msg) : new DramaException(msg);
    }

    public static DramaException occurredHandingError(Throwable e, Element elem) {
        String elemName = elem.getClass().getSimpleName();
        String msg = String.format(ELEM_HANDING_ERROR, elemName);
        return new DramaException(e, msg);
    }

    public static DramaException onlyGlobaleEvent(Class<? extends Element> elem) {
        String message = String.format(ONLY_GLOBALE_EVENT_MSG, elem.getName());
        return new DramaException(message);
    }

    public static DramaException noSpecialLayerProp(Class<? extends Layer> layer) {
        return new DramaException(String.format(NO_SPECIAL_LAYER_PROP_MSG, layer.getName()));
    }

    public static DramaException illegalLayerDesc(LayerDescription layerDesc) {
        return new DramaException(String.format(ILLEGAL_LAYER_DESC, layerDesc.desc().getName(), layerDesc.target()));
    }

    public static DramaException illegalLayerDesc(LayerDescriptor desc) {
        String msg = String.format(ILLEGAL_LAYER_DESCRIPTOR, desc.getUUID(), desc.getName());
        return new DramaException(msg);
    }
}
