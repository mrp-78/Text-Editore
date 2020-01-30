# Text-Editor
Simple text editor like vim with implementation of <b>"piece table"</b>.
## How to use?
After compiling the code if you want to open a file with text editor run the code like this:
```
java Main vim filename.txt
```
and if you want to open empty text editor run code without any parameter:
```
java Main
```
note: please run code in your terminal to have the best result
## Modes

* <b> ```Command mode``` </b> :  default mode that get commands from user

* <b> ```Insert mode``` </b> :  insert user inputs into text

* <b> ```Statistics mode``` </b> :  show number of lines and words exist in text

* <b> ```Search Mode``` </b> : show results of search

   > shows line index and number of rpeats of word in that line

Write <b> ```ESC``` </b> to exit from each mode and goto default mode.

## Commands
note : This command work only in command mode.
 * <b> ```i``` </b> : go to insert mode
 * <b> ```R``` </b> : move cursor right
 * <b> ```L``` </b> : move cursor left
 * <b> ```0``` </b> : move cursor to start of line
 * <b> ```$``` </b> : move cursor to end of line
 * <b> ```:0``` </b> : move cursor to start of file
 * <b> ```:$``` </b> : move cursor to end of file
 * <b> ```:[i]``` </b> : move cursor to start of i'th line
 
    > for exampe ```:3``` moves cursor to start of 3'th line
 
    > note: line indexes started from 0
    
 * <b> ```:w``` </b> : move cursor to start of next word
 * <b> ```:w[i]``` </b> : run previous command for i times
 * <b> ```:b``` </b> : move cursor to start of previous word
 * <b> ```:b[i]``` </b> : run previous command for i times
 * <b> ```:D``` </b> : delete chars from cursor index to end of line 
 * <b> ```:dd``` </b> : delete line that cursor is there
 * <b> ```:Y``` </b> : copy chars from cursor index to end of line
 * <b> ```:yy``` </b> : copy line that cursor is there
 * <b> ```/[word]``` </b> : search word in text and show results
 * <b> ```:q``` </b> : quit
 * <b> ```:wq``` </b> : ‫‪save to file ‫‪and‬‬ ‫‪quit‬‬
 
