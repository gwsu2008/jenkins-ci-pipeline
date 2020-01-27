def readCommitId(workDir) {
  dir(workDir) {
    def commit_id = sh(script: "${WORKSPACE}/pipeline/scripts/get-commit-id.sh ${workDir}", returnStdout: true)
    return commit_id.trim()
  }
}