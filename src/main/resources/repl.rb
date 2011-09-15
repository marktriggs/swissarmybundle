#!/usr/bin/env jruby

require 'irb'
require 'irb/input-method'
require 'irb/output-method'
require 'irb/frame'
require 'socket'

class SocketInputMethod < IRB::InputMethod
    def initialize(port = 4010)
        super

        server = TCPServer.new("127.0.0.1", port)
        @sock = server.accept
        @sock.puts("\nWelcome!  Type ':eof' to end your session.\n\n")
        @finished = false
        server.close
    end

    def gets
        @sock.write(@prompt)

        line = @sock.gets

        if (line == ":eof\r\n")
            @finished = true
            @sock.close
            return nil
        end

        return line
    end

    def eof?
        return @finished
    end

    def readable_atfer_eof?
        true
    end

    def print(*opts)
        if @sock
            @sock.puts(*opts)
        end
    end
end



module IRB
    class Context
        attr_reader :output_method

        def verbose?
            return true
        end
    end

    class Irb
        def output_value
            @context.output_method.print(sprintf(@context.return_format, @context.last_value))
        end
    end

    @CONF[:PROMPT_MODE] = :DEFAULT

end


def start_repl(port)
    print "Listening on port #{port}\n"
    io = SocketInputMethod.new(port)

    IRB.init_config(nil)
    irb = IRB::Irb.new(nil, io, io)
    IRB.conf[:MAIN_CONTEXT] = irb.context
    irb.eval_input
end
