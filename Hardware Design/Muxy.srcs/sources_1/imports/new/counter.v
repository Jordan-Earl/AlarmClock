`timescale 1ns / 1ps

module counter_tb();

reg [5:0] led;
reg [2:0] select;
wire q;

integer i;

Mux_Test test( 
select,
led,
q
);

initial begin
    led = 0;
    select = 0;
   #1 $monitor("LED: %b", led, " Selection: ", select, " Output: ", q);
    for (i = 0; i < 6; i = i + 1) 
    begin
        led[i] = 1;
        select = 0; #1;
        select = 1; #1;
        select = 2; #1;
        select = 3; #1;
        select = 4; #1;
        select = 5; #1;
        $display("+++++++++++++++++++++++++++++++++");
        led = 0;
    end
    #10 $finish;
end


endmodule
