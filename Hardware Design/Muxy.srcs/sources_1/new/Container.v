`timescale 1ns / 1ps

module Container(
    input [2:0] sw,
    output[5:0]JC
    );
    
        
Mux mux(
    .Select(sw[2:0]), 
    .led(JC[5:0])
);
    
endmodule
