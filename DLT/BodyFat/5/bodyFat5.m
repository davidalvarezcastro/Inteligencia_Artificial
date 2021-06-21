function [y1] = bodyFat5(x1)
%bodyFat5 neural network simulation function.
%
% Auto-generated by MATLAB, 30-Nov-2020 18:05:41.
%
% [y1] = myNeuralNetworkFunction(x1) takes these arguments:
%   x = 13xQ matrix, input #1
% and returns:
%   y = 1xQ matrix, output #1
% where Q is the number of samples.

%#ok<*RPMT0>

% ===== NEURAL NETWORK CONSTANTS =====

% Input 1
x1_step1.xoffset = [22;118.5;29.5;31.1;79.3;69.4;85;47.2;33;19.1;24.8;21;15.8];
x1_step1.gain = [0.0338983050847458;0.00817494379726139;0.0414507772020725;0.099502487562189;0.0351493848857645;0.0254129606099111;0.0318979266347687;0.0498753117206983;0.124223602484472;0.135135135135135;0.099009900990099;0.143884892086331;0.357142857142857];
x1_step1.ymin = -1;

% Layer 1
b1 = [-3.7982946871344642759;-4.6011570754974098207;-0.27456189469967318129;0.82651829969724532887;1.019579196907012042];
IW1_1 = [-4.6240147506369391195 -5.1188826965168825467 4.4385549474786429869 -3.921535689341085984 -7.4626963602796880792 -1.5480912608274199904 -1.0671136870634259086 6.21907797319996547 2.2717231884555770804 9.9101358805875587876 -1.5458246801753945388 -0.6314184963061026945 -4.4224051863103062132;-4.18283776701768506 -0.26294824304476965837 4.7233661977817575917 -1.5824366966034517645 6.6721269652101700132 -4.3454337377459726355 -10.135650815771287014 1.9783998360400967265 5.3736791379449355688 -1.8511251573293090456 0.53330580641306224443 -4.4735639427459332751 0.75104896228419293358;-2.0538198430031848751 4.9782305593227764717 2.0003778500737308121 -5.5404442025969933283 6.4872146163927446594 -1.2985117436189128615 10.933926006455969571 2.309343662616376136 -5.7990025089701138228 -8.6691726102243293894 8.2255186672312934348 -2.9971951987596683331 -6.1842282356550279943;0.25389440854596839747 -0.92902934165109618547 -0.06534264529936362198 0.037096580028886272373 -0.54482139073256452555 2.3282325228899747316 -0.32790605821405982567 0.032826415491066018859 -0.0028683946544407756818 0.88868283862360186731 -0.2222441630703433646 0.30687377894843970161 -0.094168519851505372298;3.5445971038336048231 0.7782035081841828239 -1.4021905040286557487 -0.2620131212262579723 -0.70020230833700181083 1.9742412508981623809 5.2358872265495701726 -3.1062642415017700515 1.3354432592217526654 -3.9848757867643014308 -2.9223136234973337011 1.751021451332771095 8.889663008701297997];

% Layer 2
b2 = -0.20068651246462271276;
LW2_1 = [-0.10565969682473061453 -0.01900366374761214977 0.099816293764276392553 0.61906407595849655134 -0.042252572506450987444];

% Output 1
y1_step1.ymin = -1;
y1_step1.gain = 0.0421052631578947;
y1_step1.xoffset = 0;

% ===== SIMULATION ========

% Dimensions
Q = size(x1,2); % samples

% Input 1
xp1 = mapminmax_apply(x1,x1_step1);

% Layer 1
a1 = tansig_apply(repmat(b1,1,Q) + IW1_1*xp1);

% Layer 2
a2 = repmat(b2,1,Q) + LW2_1*a1;

% Output 1
y1 = mapminmax_reverse(a2,y1_step1);
end

% ===== MODULE FUNCTIONS ========

% Map Minimum and Maximum Input Processing Function
function y = mapminmax_apply(x,settings)
y = bsxfun(@minus,x,settings.xoffset);
y = bsxfun(@times,y,settings.gain);
y = bsxfun(@plus,y,settings.ymin);
end

% Sigmoid Symmetric Transfer Function
function a = tansig_apply(n,~)
a = 2 ./ (1 + exp(-2*n)) - 1;
end

% Map Minimum and Maximum Output Reverse-Processing Function
function x = mapminmax_reverse(y,settings)
x = bsxfun(@minus,y,settings.ymin);
x = bsxfun(@rdivide,x,settings.gain);
x = bsxfun(@plus,x,settings.xoffset);
end
