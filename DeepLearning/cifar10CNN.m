%
% Ejemplo Aprendizaje Profundo
%
% Inteligencia Artificial
% Máster Universitario en Ingeniería Informática
% Universidad de Burgos
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



function [net] = cifar10CNN(iteraciones)


    for InitialLearnRate = [0.011]
        for LearnRateDropFactor = [0.1 0.11]
            for iteracion=1:iteraciones
                %% Diseño de la red neuronal convolucional

                % Definicion de las capas que conforman la red (13 ocultas)
                convol1 = convolution2dLayer(5,32,'Padding',2,...
                                    'BiasLearnRateFactor',2);
                convol1.Weights = gpuArray(single(randn([5 5 3 32])*0.0001));
                convol2 = convolution2dLayer(5,32,'Padding',2,'BiasLearnRateFactor',2);
                convol3 = convolution2dLayer(5,64,'Padding',2,'BiasLearnRateFactor',2);

                pooling1 = maxPooling2dLayer(3,'Stride',2);
                pooling2 = averagePooling2dLayer(3,'Stride',2);
                pooling3 = averagePooling2dLayer(3,'Stride',2);

                fullyconn1 = fullyConnectedLayer(64,'BiasLearnRateFactor',2);
                fullyconn1.Weights = gpuArray(single(randn([64 576])*0.1));
                fullyconn2 = fullyConnectedLayer(8,'BiasLearnRateFactor',2);
                fullyconn2.Weights = gpuArray(single(randn([8 64])*0.1));


                layers = [ ...
                    imageInputLayer([32 32 3]); % 32x32x3 es el tama�o de las imagenes
                    convol1;
                    pooling1;
                    reluLayer();
                    convol2;
                    reluLayer();
                    pooling2;
                    convol3;
                    reluLayer();
                    pooling3;
                    fullyconn1;
                    reluLayer();
                    fullyconn2;
                    softmaxLayer()
                    classificationLayer()];

                % Establecimiento de la opciones de entrenamiento
                opts = trainingOptions('sgdm', ...
                    'Momentum', 0.5, ...
                    'InitialLearnRate', InitialLearnRate, ...
                    'LearnRateDropFactor', LearnRateDropFactor, ...
                    'LearnRateDropPeriod', 2, ...
                    'MaxEpochs', 10, ...
                    'LearnRateSchedule', 'piecewise', ...
                    'L2Regularization', 0.004, ...
                    'MiniBatchSize', 100, ...
                    'Verbose', true, ...
                    'ExecutionEnvironment', 'parallel'); 
                                                

                %% Entrenamiento de la red

                % Definicion de los datos de entrenamiento
                imdsTrain = imageDatastore(fullfile(pwd,'cifar10Train'),...
                    'IncludeSubfolders',true,'LabelSource','foldernames');

                % Guardamos la salida del entrenamiento en un fichero
                pathDiario = ['cifer10/' num2str(InitialLearnRate) '_' num2str(LearnRateDropFactor) '/salida_' num2str(iteracion)];
                diary off
                diary(pathDiario)
                diary on
                tInicio = tic;
                [net, info] = trainNetwork(imdsTrain, layers, opts);
                disp(strcat('El tiempo de entrenamiento (en segundos) ha sido:', num2str(toc(tInicio))));
                diary off

                %%  Visualizacion de los presos de la primera capa
                % figure;
                % montage(mat2gray(gather(net.Layers(2).Weights)));
                % title('Pesos de la primera capa');


                %% Explotacion de la red

                imdsTest = imageDatastore(fullfile(pwd, 'cifar10Test'),...
                    'IncludeSubfolders',true,'LabelSource','foldernames');
                YTest = classify(net, imdsTest);

                labels = {'airplane' 'automobile' 'bird' 'dog' 'frog' 'horse' 'ship' 'truck'};
                for i=1:8
                    YTestClasses(i,:) = (YTest == labels{i});
                    TTestClasses(i,:) = (imdsTest.Labels == labels{i});
                end

                % Generacion de la matriz de confusion
                matrizConfusion = figure;
                figMatrizConfusion = plotconfusion(double(TTestClasses),double(YTestClasses))

                imgName = ['cifer10/' num2str(InitialLearnRate) '_' num2str(LearnRateDropFactor) '/learning_rate_' num2str(InitialLearnRate) '_learning_rate_drop_factor_' num2str(LearnRateDropFactor)  '_iteracion_' num2str(iteracion) '.jpg']
                saveas(figMatrizConfusion, imgName, 'jpeg');
                close(figMatrizConfusion)


                % Muestra de algunos datos
                % [YTest(1:10)';YTest(1001:1010)';YTest(2001:2010)';YTest(3001:3010)';YTest(4001:4010)';YTest(5001:5010)';YTest(6001:6010)';YTest(7001:7010)';YTest(8001:8010)';YTest(9001:9010)']
                % figure;
                % montage({imdsTest.Files{1:10},imdsTest.Files{1001:1010},imdsTest.Files{2001:2010},imdsTest.Files{3001:3010},imdsTest.Files{4001:4010},imdsTest.Files{5001:5010},imdsTest.Files{6001:6010},imdsTest.Files{7001:7010},imdsTest.Files{8001:8010},imdsTest.Files{9001:9010}})
                % title('Primeras imagenes de testeo de cada clase');
            end
        end
    end
end
