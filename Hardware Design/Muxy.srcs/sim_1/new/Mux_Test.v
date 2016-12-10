`timescale 1ns / 1ps

module Mux_Test(
input wire[2:0] Select,
input wire[5:0] led,
output reg q
);


always @(led or Select) begin
  if (Select == 0) begin
     q = led[0];
  end
  
  if (Select == 1) begin
       q = led[1];
  end
  
  if (Select == 2) begin
         q = led[2];
  end
  
  if (Select == 3) begin
         q = led[3];
  end
  
  if (Select == 4) begin
         q = led[4];
  end
  
  if (Select == 5) begin
         q = led[5];
    end
end

    
endmodule
