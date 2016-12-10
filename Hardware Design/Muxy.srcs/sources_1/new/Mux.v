`timescale 1ns / 1ps


module Mux(
    input wire[2:0] Select,
    output reg[5:0] led
    );
    
   
    always @(Select) begin
      if (Select == 0) begin
        led 
        = 6'b111111;
      end
      if (Select == 1) begin
       led = 6'b111110;
      end
      
      if (Select == 2) begin
        led = 6'b111101;
      end
      
      if (Select == 3) begin
        led = 6'b111011;
      end
      
      if (Select == 4) begin
         led = 6'b110111;
        
      end
      
      if (Select == 5) begin
         led = 6'b101111;
      end
      
      if (Select == 6) begin
         led = 6'b011111;
        end
    end

    
endmodule
