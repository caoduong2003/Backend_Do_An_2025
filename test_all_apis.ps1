# Script test tất cả API endpoints
# Chạy: .\test_all_apis.ps1

$baseUrl = "http://localhost:8080"
$results = @()

Write-Host "=== KIỂM TRA TẤT CẢ API ENDPOINTS ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host ""

# 1. AUTHENTICATION
Write-Host "1. Testing Authentication APIs..." -ForegroundColor Cyan
$authEndpoints = @(
    @{Method="POST"; Path="/api/auth/dangky"; Body='{"tenDangNhap":"test","matKhau":"123456","hoTen":"Test User","email":"test@test.com"}'},
    @{Method="POST"; Path="/api/auth/dangnhap"; Body='{"tenDangNhap":"admin","matKhau":"123456"}'}
)

foreach ($endpoint in $authEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -Body $endpoint.Body -ContentType "application/json" -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 2. BÀI GIẢNG
Write-Host "`n2. Testing BaiGiang APIs..." -ForegroundColor Cyan
$baigiangEndpoints = @(
    @{Method="GET"; Path="/api/baigiang"},
    @{Method="GET"; Path="/api/baigiang/1"},
    @{Method="GET"; Path="/api/baigiang/search?keyword=test"},
    @{Method="GET"; Path="/api/baigiang/level/HSK1"}
)

foreach ($endpoint in $baigiangEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 3. TIẾN TRÌNH
Write-Host "`n3. Testing TienTrinh APIs..." -ForegroundColor Cyan
$tienTrinhEndpoints = @(
    @{Method="GET"; Path="/api/tien-trinh"},
    @{Method="GET"; Path="/api/tien-trinh/statistics"},
    @{Method="GET"; Path="/api/tien-trinh/recent"},
    @{Method="GET"; Path="/api/tien-trinh/top-scores"}
)

foreach ($endpoint in $tienTrinhEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 4. TỪ VỰNG
Write-Host "`n4. Testing TuVung APIs..." -ForegroundColor Cyan
$tuVungEndpoints = @(
    @{Method="GET"; Path="/api/tuvung"},
    @{Method="GET"; Path="/api/tuvung/1"},
    @{Method="GET"; Path="/api/tuvung/baigiang/1"},
    @{Method="GET"; Path="/api/tuvung/search?keyword=test"}
)

foreach ($endpoint in $tuVungEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 5. CHỦ ĐỀ
Write-Host "`n5. Testing ChuDe APIs..." -ForegroundColor Cyan
$chuDeEndpoints = @(
    @{Method="GET"; Path="/api/chude"},
    @{Method="GET"; Path="/api/chude/1"},
    @{Method="GET"; Path="/api/chude/search?keyword=test"}
)

foreach ($endpoint in $chuDeEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 6. CẤP ĐỘ HSK
Write-Host "`n6. Testing CapDoHSK APIs..." -ForegroundColor Cyan
$capDoHSKEndpoints = @(
    @{Method="GET"; Path="/api/capdohsk"},
    @{Method="GET"; Path="/api/capdohsk/1"},
    @{Method="GET"; Path="/api/capdohsk/level/HSK1"},
    @{Method="GET"; Path="/api/capdohsk/search?keyword=test"}
)

foreach ($endpoint in $capDoHSKEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 7. LOẠI BÀI GIẢNG
Write-Host "`n7. Testing LoaiBaiGiang APIs..." -ForegroundColor Cyan
$loaiBaiGiangEndpoints = @(
    @{Method="GET"; Path="/api/loaibaigiang"},
    @{Method="GET"; Path="/api/loaibaigiang/1"},
    @{Method="GET"; Path="/api/loaibaigiang/search?keyword=test"}
)

foreach ($endpoint in $loaiBaiGiangEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 8. TRANSLATION
Write-Host "`n8. Testing Translation APIs..." -ForegroundColor Cyan
$translationEndpoints = @(
    @{Method="POST"; Path="/api/translation/vi-to-zh"; Body='"Xin chào"'},
    @{Method="POST"; Path="/api/translation/zh-to-vi"; Body='"你好"'}
)

foreach ($endpoint in $translationEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -Body $endpoint.Body -ContentType "text/plain" -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 9. MEDIA
Write-Host "`n9. Testing Media APIs..." -ForegroundColor Cyan
$mediaEndpoints = @(
    @{Method="GET"; Path="/api/media/video/test.mp4/info"},
    @{Method="GET"; Path="/api/media/image/test.jpg"}
)

foreach ($endpoint in $mediaEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# 10. FILES
Write-Host "`n10. Testing Files APIs..." -ForegroundColor Cyan
$filesEndpoints = @(
    @{Method="GET"; Path="/api/files/test.txt"},
    @{Method="GET"; Path="/api/files/debug/list"}
)

foreach ($endpoint in $filesEndpoints) {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Status="❌ FAILED"; Code="$statusCode"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
    }
}

# Tổng kết
Write-Host "`n=== TỔNG KẾT KẾT QUẢ ===" -ForegroundColor Green
$successCount = ($results | Where-Object {$_.Status -eq "✅ SUCCESS"}).Count
$failCount = ($results | Where-Object {$_.Status -eq "❌ FAILED"}).Count
$totalCount = $results.Count

Write-Host "Tổng số API tested: $totalCount" -ForegroundColor Yellow
Write-Host "✅ Thành công: $successCount" -ForegroundColor Green
Write-Host "❌ Thất bại: $failCount" -ForegroundColor Red

if ($failCount -gt 0) {
    Write-Host "`n=== CHI TIẾT LỖI ===" -ForegroundColor Red
    $results | Where-Object {$_.Status -eq "❌ FAILED"} | Format-Table -AutoSize
}

Write-Host "`n=== TẤT CẢ KẾT QUẢ ===" -ForegroundColor Yellow
$results | Format-Table -AutoSize

# Lưu kết quả vào file
$results | Export-Csv -Path "api_test_results.csv" -NoTypeInformation -Encoding UTF8
Write-Host "`nKết quả đã được lưu vào file: api_test_results.csv" -ForegroundColor Cyan 