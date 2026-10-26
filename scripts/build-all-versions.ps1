$ErrorActionPreference = "Stop"
$env:JAVA_HOME = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { "C:\Program Files\Java\jdk-25.0.3" }
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$Root = Split-Path -Parent $PSScriptRoot
if (-not (Test-Path (Join-Path $Root "gradlew.bat"))) {
    $Root = "e:\Mods Minecraft\LetterRush"
}
Set-Location $Root

# Supported release targets only
$versions = @(
    "1.21.11",
    "26.1.2",
    "26.2",
    "26.3"
)

$log = Join-Path $Root "release\build-all.log"
New-Item -ItemType Directory -Force -Path (Join-Path $Root "release") | Out-Null
"" | Set-Content $log -Encoding utf8

function Merge-Props([string]$mc) {
    $common = Join-Path $Root "gradle\versions\common.properties"
    $ver = Join-Path $Root "gradle\versions\$mc.properties"
    Get-Content $common, $ver | Set-Content (Join-Path $Root "gradle.properties") -Encoding utf8
}

$failed = [System.Collections.Generic.List[string]]::new()
foreach ($mc in $versions) {
    Write-Host "========== Minecraft $mc =========="
    Add-Content $log "========== Minecraft $mc =========="
    Merge-Props $mc
    $props = Get-Content (Join-Path $Root "gradle.properties") -Raw
    $skipNeo = $props -match '(?m)^skip_neoforge=true'
    $ErrorActionPreference = "Continue"
    if ($skipNeo) {
        & .\gradlew.bat --no-daemon clean :fabric:remapJar 2>&1 | Tee-Object -FilePath $log -Append
    } else {
        & .\gradlew.bat --no-daemon clean :fabric:remapJar :neoforge:remapJar 2>&1 | Tee-Object -FilePath $log -Append
    }
    $code = $LASTEXITCODE
    $ErrorActionPreference = "Stop"
    if ($code -ne 0) {
        $failed.Add($mc) | Out-Null
        Add-Content $log "FAILED $mc exit=$code"
        Write-Host "FAILED $mc"
        continue
    }
    $out = Join-Path $Root "release\$mc"
    New-Item -ItemType Directory -Force -Path $out | Out-Null
    Get-ChildItem $out -Filter "*.jar" -ErrorAction SilentlyContinue | Remove-Item -Force
    Copy-Item (Join-Path $Root "fabric\build\libs\letterrush-1.0.0-$mc+fabric.jar") $out -Force
    if (-not $skipNeo) {
        $neoJar = Join-Path $Root "neoforge\build\libs\letterrush-1.0.0-$mc+neoforge.jar"
        if (Test-Path $neoJar) {
            Copy-Item $neoJar $out -Force
        }
    }
    Get-ChildItem $out | ForEach-Object {
        Write-Host ("  " + $_.Name)
        Add-Content $log ("  " + $_.Name)
    }
}

Merge-Props "26.3"
$failMsg = "FAILED: " + ($failed -join ", ")
$failMsg | Tee-Object -FilePath $log -Append
"DONE" | Tee-Object -FilePath $log -Append
if ($failed.Count -gt 0) {
    exit 1
}
