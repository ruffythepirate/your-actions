const read = require('read-yaml');
const fs = require('fs');
const os = require('os');
const {spawn} = require('child_process');

const readline = require('readline');

const configFile = './example/build.yaml';

async function loadStepConfiguration(filename) {
  const configFile = await read.sync(filename);
  return configFile;
}

async function generateStepFiles(stepConfiguration) {
  await writeBuildScript(stepConfiguration.script);
  await writeDockerfile(stepConfiguration.from);
}

async function writeBuildScript(buildScript) {

  fs.writeFileSync('./build.sh', buildScript)
}

async function writeDockerfile(from) {
  const statements = [];
  statements.push(`FROM ${from}`);
  statements.push(`WORKDIR /var/executor`);
  statements.push(`COPY build.sh /var/executor/build.sh`);
  statements.push(`CMD sh /var/executor/build.sh`);

  fs.writeFileSync('Dockerfile', statements.join(os.EOL))
}

async function buildDocker() {
  await runCommand('docker build -t exector-test:test .');
}

async function runDocker() {
  await runCommand('docker run exector-test:test');
}

function runCommand(fullCommand) {
    return new Promise((resolve, reject) => {
      const commandParts = fullCommand.split(' ');
        const command = commandParts[0]
        const compileProcess = spawn(
            command,
            commandParts.slice(1),
            {
                shell: true
            });
        compileProcess.on('close', (code) => {
            if(code === 0) {
                resolve();
            } else {
                reject(`Process finished with exit code ${code}`)
            }
        });
        connectOutputs(compileProcess, console.log, console.error);


  })
}

function connectOutputs(childProcess, stdout, stderr) {
   childProcess.stdout && createInterface(childProcess.stdout, stdout);
   childProcess.stderr && createInterface(childProcess.stderr, stderr);
}

function createInterface(input, output) {
    const rlInterface = readline.createInterface ( {
        input: input
    });
    rlInterface.on ( 'line', output);
}

(async () => {
  const config = await loadStepConfiguration(configFile)
  await writeBuildScript(config.steps[0].script);
  await writeDockerfile(config.steps[0].from);
  await buildDocker();
  await runDocker();
})()
