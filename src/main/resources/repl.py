#!/usr/bin/env python

import traceback
import sys
import code
import socket

def handle_connection(io):
    sys.stdout = io
    sys.stderr = io

    globaldict = globals()

    try:
        print "Type ':eof' to exit"

        while True:
            expression = ""

            print "\n>>> ",

            while True:
                io.flush()
                next = io.readline().rstrip()

                if (next == ":eof"):
                    return

                if expression:
                    expression += ("\n" + next)
                else:
                    expression = next

                res = None
                try:
                    res = code.compile_command(expression)
                except:
                    io.write(traceback.print_exc())                    
                    break

                if res:
                    try:
                        localdict = {}
                        exec res in globaldict, localdict
                        globaldict.update(localdict)
          
                    except:
                        io.write(traceback.print_exc())

                    expression = ""
                    break
                else:
                    # Drop the trailing continuation char (\) if we have one
                    if expression[-1] == '\\':
                        expression = expression[:-1]
                    print "\n... ",
    except EOFError:
        pass

    sys.stdout = sys.__stdout__
    sys.stderr = sys.__stderr__



def start_repl(port):
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        serversocket.bind(('localhost', port))
        serversocket.listen(1)

        print "Going to listen on port %d" % (port)
        sock = serversocket.accept()[0]
        try:
            io = sock.makefile()
            handle_connection(io)
            
        finally:
            io.close()
            sock.close()
    finally:
        serversocket.close()
