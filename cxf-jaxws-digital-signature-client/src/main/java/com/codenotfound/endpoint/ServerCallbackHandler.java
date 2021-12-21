package com.codenotfound.endpoint;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class ServerCallbackHandler implements CallbackHandler {

  @Override
  public void handle(Callback[] callbacks)
      throws IOException, UnsupportedCallbackException {
    // Do nothing
  }
}
