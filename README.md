# clj-live-reload

An embedded Clojure server for [live-reload](http://livereload.com/)

## Installation (via Clojars.org)

```[com.benrady/clj-live-reload "0.1.0"]```

## Embedding

clj-live-reload can be easily embedded in another process by calling:

```
=> (clj-live-reload.core/start-server "path/to/files")
```

This will start an http server on port 35729 (the default live-reload port) and watch the given file path (and subdirectories) for file changes. This call will block, so you may want to wrap it in a `(future)`.

## Client

Tested with the [Chrome live-reload extension](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei). 

## License

Copyright © 2018 Ben Rady

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
