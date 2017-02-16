package org.apache.mina.core.service;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public interface IoProcessor<S extends IoSession> {
    void add(S s);

    void dispose();

    void flush(S s);

    boolean isDisposing();

    void remove(S s);

    void updateTrafficControl(S s);

    void write(S s, WriteRequest writeRequest);
}