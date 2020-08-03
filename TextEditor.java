import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class TextEditor {
    int cursorLine, cursorIndex;
    String origin;
    String add;
    LinkedList lines;
    String copy;
    String fileName;
    short state; // 0:command  1:insert  2:statistics

    public TextEditor() {
        System.out.print("\033[?1049h\033[H");
        cursorLine = 0;
        cursorIndex = 0;
        add = "";
        lines = new LinkedList();
        LinkedList pieces = new LinkedList();
        LineNode ln = new LineNode(pieces);
        lines.add(ln, null);
        state = 0;
        copy = "";
        origin = "";
        fileName = "";
    }

    public void add(String txt) {
        PieceNode p = new PieceNode(add.length(), txt.length(), false);
        add = add + txt;
        if (cursorLine == lines.getSize() - 1 && cursorIndex == ((LineNode) lines.getTail()).getTxtSize()) {
            if (txt.equals("\n")) {
                lines.addToEnd(new LineNode(new LinkedList()));
                cursorIndex = 0;
                cursorLine++;
            } else {
                LineNode prev = (LineNode) lines.getTail().getPrev();
                LinkedList tail = ((LineNode) lines.getTail()).getPieces();
                tail.addToEnd(p);
                lines.remove(prev);
                LineNode ln = new LineNode(tail);
                lines.addToEnd(ln);
                cursorIndex += txt.length();
            }
        } else if (cursorLine == 0 && cursorIndex == 0) {
            if (txt.equals("\n")) {
                lines.add(new LineNode(new LinkedList()), null);
                cursorIndex = 0;
                cursorLine++;
            } else {
                LinkedList head = ((LineNode) lines.getHead()).getPieces();
                head.add(p, null);
                lines.remove(null);
                LineNode ln = new LineNode(head);
                lines.add(ln, null);
                cursorIndex += txt.length();
            }
        } else {
            int current = 0, i = 0;
            LineNode l = (LineNode) lines.getHead();
            out:
            while (l != null) {
                if (i == cursorLine) {
                    LinkedList piece = l.getPieces();
                    PieceNode h = (PieceNode) piece.getHead();
                    while (h != null) {
                        if (cursorIndex == 0) {
                            piece.add(p, null);
                            cursorIndex += txt.length();
                        } else if (current + h.getLength() >= cursorIndex) {
                            if (current + h.getLength() == cursorIndex) {
                                piece.add(p, h);
                                if (txt.equals("\n")) {
                                    PieceNode newL = p.getNext();
                                    LinkedList newLine = new LinkedList(newL);
                                    LineNode ln = new LineNode(newLine);
                                    piece.remove(h);
                                    h.setNext(null);
                                    p.setNext(null);
                                    p.setPrev(null);
                                    cursorIndex = 0;
                                    lines.add(ln, l);
                                    cursorLine++;
                                } else {
                                    cursorIndex += txt.length();
                                }
                            } else {
                                int d = cursorIndex - current;
                                PieceNode p1 = new PieceNode(h.getStart(), d, h.isOrigin());
                                PieceNode p2 = new PieceNode(d + h.getStart(), h.getLength() - d, h.isOrigin());
                                piece.add(p1, h);
                                piece.add(p, p1);
                                piece.add(p2, p);
                                if (txt.equals("\n")) {
                                    piece.remove(p1);
                                    p1.setNext(null);
                                    p.setPrev(null);
                                    p.setNext(null);
                                    LinkedList newLine = new LinkedList(p2);
                                    LineNode ln = new LineNode(newLine);
                                    lines.add(ln, l);
                                    cursorIndex = 0;
                                    cursorLine++;
                                } else {
                                    cursorIndex += txt.length();
                                }
                                piece.remove(h.getPrev());
                            }
                            LineNode prev = l.getPrev();
                            lines.remove(prev);
                            LineNode ln = new LineNode(piece);
                            lines.add(ln, prev);
                            break out;
                        }
                        current += h.getLength();
                        i++;
                        h = h.getNext();
                    }
                }
                l = l.getNext();
                i++;
            }
        }
        showTxt();
    }

    public void showTxt() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        char ch = 9619, ch2 = 9617;
        int i = 0, start = 0, current = 0;
        int txtSize = 0;
        LineNode l = (LineNode) lines.getHead();
        for (int j = 0; j < 20; j++) {
            System.out.print("" + ch2);
        }
        if (state == 0) {
            System.out.print(" Command Mode ");
        } else if (state == 1) {
            System.out.print(" Insert Mode ");
        }
        for (int j = 0; j < 20; j++) {
            System.out.print("" + ch2);
        }
        System.out.println();
        while (l != null) {
            txtSize = 0;
            PieceNode p = (PieceNode) l.getPieces().getHead();
            if (i == cursorLine) {
                if (cursorIndex == 0)
                    System.out.print(ch);
                while (p != null) {
                    txtSize += p.getLength();
                    if (current < cursorIndex && current + p.getLength() >= cursorIndex) {
                        if (current + p.getLength() == cursorIndex) {
                            start = p.getStart();
                            if (p.isOrigin())
                                System.out.print(origin.substring(start, start + p.getLength()));
                            else
                                System.out.print(add.substring(start, start + p.getLength()));
                            System.out.print(ch);
                        } else {
                            start = p.getStart();
                            int d = cursorIndex - current;
                            if (p.isOrigin()) {
                                System.out.print(origin.substring(start, start + d));
                                System.out.print(ch);
                                System.out.print(origin.substring(start + d, start + p.getLength()));
                            } else {
                                System.out.print(add.substring(start, start + d));
                                System.out.print(ch);
                                System.out.print(add.substring(start + d, start + p.getLength()));
                            }
                        }
                    } else {
                        start = p.getStart();
                        if (p.isOrigin())
                            System.out.print(origin.substring(start, start + p.getLength()));
                        else
                            System.out.print(add.substring(start, start + p.getLength()));
                    }
                    current += p.getLength();
                    p = p.getNext();
                }
            } else {
                while (p != null) {
                    txtSize += p.getLength();
                    start = p.getStart();
                    if (p.isOrigin())
                        System.out.print(origin.substring(start, start + p.getLength()));
                    else
                        System.out.print(add.substring(start, start + p.getLength()));
                    p = p.getNext();
                }
            }
            l.setTxtSize(txtSize);
            l = l.getNext();
            i++;
            System.out.println();
        }
    }

    public void inputHandler(String inp) {
        char ch2 = 9617;
        LineNode ln;
        if (inp.equals("")) {
            inp = "\n";
        }
        switch (inp.substring(0, 1)) {
            case "L":
                if (state == 0) {
                    if (cursorIndex == 0) {
                        cursorLine--;
                        cursorIndex = ((LineNode) lines.getIndex(cursorLine)).getTxtSize();
                    } else {
                        cursorIndex--;
                    }
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case "R":
                if (state == 0) {
                    ln = (LineNode) lines.getIndex(cursorLine);
                    if (cursorIndex == ln.getTxtSize()) {
                        cursorIndex = 0;
                        cursorLine++;
                    } else {
                        cursorIndex++;
                    }
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case "0":
                if (state == 0) {
                    cursorIndex = 0;
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case "$":
                if (state == 0) {
                    cursorIndex = ((LineNode) lines.getIndex(cursorLine)).getTxtSize();
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case ":":
                if (state == 0) {
                    if (inp.substring(1, 2).equals("$")) {
                        cursorLine = lines.getSize() - 1;
                        cursorIndex = ((LineNode) lines.getTail()).getTxtSize();
                    } else if (inp.equals(":q")) {
                        System.exit(0);
                    } else if (inp.equals(":wq")) {
                        if (!fileName.equals(""))
                            writeFile(fileName);
                        System.exit(0);
                    }
                    else if (inp.substring(1, 2).equals("w")) {
                        if (inp.length() > 2) {
                            gotToNextWord(Integer.parseInt(inp.substring(2)));
                        } else {
                            gotToNextWord(1);
                        }
                    } else if (inp.substring(1, 2).equals("b")) {
                        if (inp.length() > 2) {
                            gotToPrevWord(Integer.parseInt(inp.substring(2)));
                        } else {
                            gotToPrevWord(1);
                        }
                    } else if (inp.equals(":D")) {
                        if (state == 0) {
                            delete();
                            showTxt();
                        } else if (state == 1) {
                            add(inp);
                        }
                    } else if (inp.equals(":dd")) {
                        cursorLine--;
                        lines.remove(lines.getIndex(cursorLine));
                        if (lines.size == 0) {
                            lines.add(new LineNode(new LinkedList()), null);
                        }
                        if (cursorLine < 0) {
                            cursorLine = 0;
                            cursorIndex = 0;
                        } else {
                            cursorIndex = ((LineNode) lines.getIndex(cursorLine)).getTxtSize();
                        }
                    } else if (inp.equals(":Y")) {
                        copy(false);
                    } else if (inp.equals(":yy")) {
                        copy(true);
                    } else if (inp.equals(":p")) {
                        add(copy);
                    } else {
                        cursorLine = Integer.parseInt(inp.substring(1));
                        cursorIndex = 0;
                    }
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case "i":
                if (state == 0)
                    state = 1;
                else if (state == 1)
                    add(inp);
                showTxt();
                break;
            case "v":
                if (state == 0) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    for (int j = 0; j < 20; j++) {
                        System.out.print("" + ch2);
                    }
                    System.out.print(" Statistics Mode ");
                    for (int j = 0; j < 20; j++) {
                        System.out.print("" + ch2);
                    }
                    System.out.println();
                    System.out.println("Lines: " + lines.getSize());
                    System.out.println("Words: " + countWords());
                    state = 3;
                } else if (state == 1) {
                    add(inp);
                }
                break;
            case "e":
                if (state == 0) {
                    if (inp.equals("esc"))
                        state = 0;
                } else if (state == 1) {
                    add(inp);
                }
                showTxt();
                break;
            case "E":
                if (inp.equals("ESC")) {
                    state = 0;
                }
                showTxt();
                break;
            case "/":
                if (state == 0) {
                    search(inp.substring(1));
                } else if (state == 1) {
                    add(inp);
                }
                break;
            default:
                if (state == 1) {
                    add(inp);
                }
        }
    }

    public void delete() {
        if (cursorIndex == 0) {
            cursorLine--;
            lines.remove(lines.getIndex(cursorLine));
            if (lines.size == 0) {
                lines.add(new LineNode(new LinkedList()), null);
            }
            if (cursorLine < 0) {
                cursorLine = 0;
                cursorIndex = 0;
            } else {
                cursorIndex = ((LineNode) lines.getIndex(cursorLine)).getTxtSize();
            }
        } else {
            LinkedList pieces = ((LineNode) lines.getIndex(cursorLine)).getPieces();
            PieceNode p = (PieceNode) pieces.getHead();
            int current = 0;
            while (p != null) {
                if (current + p.getLength() >= cursorIndex) {
                    if (current + p.getLength() == cursorIndex) {
                        PieceNode next = p.getNext();
                        p.setNext(null);
                        next.setPrev(null);
                    } else {
                        int d = cursorIndex - current;
                        PieceNode p1 = new PieceNode(p.getStart(), d, p.isOrigin());
                        PieceNode prev = p.getPrev();
                        pieces.remove(prev);
                        pieces.add(p1, prev);
                        if (p1.getNext() != null)
                            p1.getNext().setPrev(null);
                        p1.setNext(null);
                    }
                }
                current += p.getLength();
                p = p.getNext();
            }
        }
    }

    public int countWords() {
        int count = 0;
        String line;
        LineNode l = (LineNode) lines.getHead();
        while (l != null) {
            line = makeLine(l);
            if (!line.equals(""))
                count += makeLine(l).split("\\s+").length;
            l = l.getNext();
        }
        return count;
    }

    private String makeLine(LineNode node) {
        String res = "";
        int start;
        PieceNode p = (PieceNode) node.getPieces().getHead();
        while (p != null) {
            start = p.getStart();
            if (p.isOrigin())
                res += origin.substring(start, start + p.getLength());
            else
                res += add.substring(start, start + p.getLength());
            p = p.getNext();
        }
        return res;
    }

    public void search(String txt) {
        boolean found = false;
        char ch2 = 9617;
        int count;
        int i = 0;
        LineNode l = (LineNode) lines.getHead();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int j = 0; j < 20; j++) {
            System.out.print("" + ch2);
        }
        System.out.print(" Search Mode ");
        for (int j = 0; j < 20; j++) {
            System.out.print("" + ch2);
        }
        System.out.println();
        System.out.println("word: " + txt);
        while (l != null) {
            count = makeLine(l).split(txt, -1).length - 1;
            if (count > 0) {
                System.out.println(i + ": " + count);
                found = true;
            }
            l = l.getNext();
            i++;
        }
        if (!found) {
            System.out.println("Not found :(");
        }
    }

    public void gotToNextWord(int n) {
        String line = makeLine((LineNode) lines.getIndex(cursorLine));
        for (int i = 0; i < n; i++) {
            int index = line.substring(cursorIndex).indexOf(" ");
            if (index == -1) {
                if (cursorLine < lines.getSize() - 1) {
                    cursorLine++;
                    cursorIndex = 0;
                    line = makeLine((LineNode) lines.getIndex(cursorLine));
                } else {
                    cursorIndex = line.length();
                }
            } else {
                cursorIndex += index + 1;
            }
        }
    }

    public void gotToPrevWord(int n) {
        String line = makeLine((LineNode) lines.getIndex(cursorLine));
        for (int i = 0; i < n; i++) {
            int index = line.substring(0, cursorIndex).lastIndexOf(" ");
            if (index != -1)
                index = line.substring(0, index).lastIndexOf(" ");
            if (index == -1) {
                if (cursorLine > 0) {
                    cursorLine--;
                    line = makeLine((LineNode) lines.getIndex(cursorLine));
                    index = line.lastIndexOf(" ");
                    if (index != -1)
                        cursorIndex = index + 1;
                    else
                        cursorIndex = 0;
                } else {
                    cursorIndex = 0;
                }
            } else {
                cursorIndex = index + 1;
            }
        }
    }

    public void copy(boolean fromFirst) {
        copy = makeLine((LineNode) lines.getIndex(cursorLine));
        if (!fromFirst) {
            copy = copy.substring(cursorIndex);
        }
        System.out.println(copy);
    }

    public void readFile(String fileName) {
        this.fileName = fileName;
        lines.remove(null);
        String line;
        LineNode ln;
        PieceNode pn;
        try (FileReader fileReader = new FileReader(fileName);
             Scanner scanner2 = new Scanner(fileReader)) {
            while (scanner2.hasNext()) {
                line = scanner2.nextLine();
                pn = new PieceNode(origin.length(), line.length(), true);
                ln = new LineNode(new LinkedList(pn));
                ln.setTxtSize(line.length());
                lines.addToEnd(ln);
                origin += line;
            }
            cursorLine = lines.getSize()-1;
            cursorIndex = ((LineNode)lines.getIndex(cursorLine)).getTxtSize();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showTxt();
    }

    public void writeFile(String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(makeFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makeFile() {
        String res = "";
        LineNode l = (LineNode) lines.getHead();
        PieceNode p;
        while (l != null) {
            p = (PieceNode) l.getPieces().getHead();
            while (p != null) {
                if (p.isOrigin()) {
                    res += origin.substring(p.getStart(), p.getStart() + p.getLength());
                } else {
                    res += add.substring(p.getStart(), p.getStart() + p.getLength());
                }
                p = p.getNext();
            }
            res += "\n";
            l = l.getNext();
        }
        return res;
    }
}
