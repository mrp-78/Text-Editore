# ✏️ Text-Editore

A minimal, terminal-based text editor inspired by **vim**, built around the **piece table** data structure for fast edits and undo/redo.

## Features

* **Vim-like modal editing**: Command, Insert, Search, and Statistics modes
* **Piece Table core**: efficient inserts/deletes without copying whole buffers
* **Cursor navigation & word motions**
* **Search with per-line hit counts**
* **Basic clipboard**: copy/yank and paste lines or ranges
* **Save & quit** commands

## Quick Start

### Prerequisites

* Java 8+ (JDK)

### Build

```bash
javac Main.java
```
### Run

Open a file directly:

```bash
java Main vim <filename.txt>
```

Start with an empty buffer:

```bash
java Main
```

> Tip: Run in a real terminal for the best experience.

## Modes

* **Command mode**: default, accepts editor commands
* **Insert mode**: inserts user input into the text
* **Statistics mode**: shows number of lines and words
* **Search mode**: shows matches with line index and repeats

Press **`ESC`** to leave any mode and return to Command.

## Commands & Keys (Command mode)

| Key / Command | Action                                        |
| ------------- | --------------------------------------------- |
| `i`           | Enter Insert mode                             |
| `R`           | Move cursor right                             |
| `L`           | Move cursor left                              |
| `0`           | Move cursor to start of line                  |
| `$`           | Move cursor to end of line                    |
| `:0`          | Move cursor to start of file                  |
| `:$`          | Move cursor to end of file                    |
| `:[i]`        | Move cursor to start of *i*-th line (0-based) |
| `:w`          | Move to start of next word                    |
| `:w[i]`       | Repeat previous command *i* times             |
| `:b`          | Move to start of previous word                |
| `:b[i]`       | Repeat previous command *i* times             |
| `:D`          | Delete from cursor to end of line             |
| `:dd`         | Delete current line                           |
| `:Y`          | Copy from cursor to end of line               |
| `:yy`         | Copy current line                             |
| `:p`          | Paste                                         |
| `/word`       | Search for `word` and show results            |
| `:q`          | Quit                                          |
| `:wq`         | Save and quit                                 |

## Architecture

### Why a Piece Table?

This editor keeps two text buffers:

* **origin**: the immutable text loaded from the file
* **add**: an append‑only buffer for all user insertions

Each line of the visible document is represented by a list of **pieces**. A piece points to a span in either `origin` or `add` with `(start, length, isOrigin)`. Editing rearranges pieces (or splits a piece into two) instead of copying large strings, which keeps inserts/deletes efficient. Newlines create/merge **LineNode** rows.

### Project Structure

```
.
├── Main.java            # entry point
├── TextEditor.java      # core editor logic (piece table, cursor, modes, rendering)
├── PieceNode.java       # piece table node (origin/add buffer span)
├── LineNode.java        # represents a line (collection of pieces)
└── LinkedList.java      # utility linked list for nodes
```

### High‑level components

* **Main** — program entry point. Parses optional args (e.g., `vim <file>`), instantiates the editor, renders once, and forwards terminal input lines to the editor.
* **TextEditor** — the core state machine and renderer:

  * Cursor: `cursorLine`, `cursorIndex`
  * Buffers: `origin` (file), `add` (insertions)
  * Document: `lines` (a linked list of `LineNode`), each `LineNode` contains a linked list of `PieceNode`
  * Modes: Command (default), Insert, plus on‑demand **Statistics** and **Search** screens
  * Handlers: `inputHandler`, `add`, `delete`, `search`, `gotToNextWord`, `gotToPrevWord`, file I/O (`readFile`, `writeFile`)
  * Rendering: `showTxt()` draws the header and body, highlights the cursor, and recomputes per‑line text sizes.
* **Data structures** — a tiny doubly‑linked list framework:

  * `Node<E>`: interface with `next/prev` pointers
  * `LinkedList`: operations `add`, `addToEnd`, `remove`, `getIndex`, `getHead/Tail`, `size`
  * `LineNode`: holds `pieces` (list of `PieceNode`) and `txtSize`
  * `PieceNode`: piece‑table cell with `start`, `length`, `isOrigin`, and `next/prev`

