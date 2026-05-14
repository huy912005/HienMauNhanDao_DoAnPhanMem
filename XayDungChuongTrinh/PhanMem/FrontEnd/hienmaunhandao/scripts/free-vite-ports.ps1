# Stops Node processes listening on typical Vite dev ports (npm run dev runs this via predev).
$ErrorActionPreference = 'SilentlyContinue'
$portMin = 5173
$portMax = 5190
$pidsSeen = @{}

foreach ($port in $portMin..$portMax) {
    $listeners = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    foreach ($listener in $listeners) {
        $owningPid = $listener.OwningProcess
        if (-not $owningPid -or $pidsSeen.ContainsKey($owningPid)) { continue }
        $proc = Get-Process -Id $owningPid -ErrorAction SilentlyContinue
        if ($proc -and ($proc.ProcessName -eq 'node')) {
            Write-Host "[free-vite-ports] Stopping node.exe PID $owningPid (was listening on port $port)"
            Stop-Process -Id $owningPid -Force -ErrorAction SilentlyContinue
            $pidsSeen[$owningPid] = $true
        }
    }
}
