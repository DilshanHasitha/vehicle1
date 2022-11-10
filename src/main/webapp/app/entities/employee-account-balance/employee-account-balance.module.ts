import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeAccountBalanceComponent } from './list/employee-account-balance.component';
import { EmployeeAccountBalanceDetailComponent } from './detail/employee-account-balance-detail.component';
import { EmployeeAccountBalanceUpdateComponent } from './update/employee-account-balance-update.component';
import { EmployeeAccountBalanceDeleteDialogComponent } from './delete/employee-account-balance-delete-dialog.component';
import { EmployeeAccountBalanceRoutingModule } from './route/employee-account-balance-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeAccountBalanceRoutingModule],
  declarations: [
    EmployeeAccountBalanceComponent,
    EmployeeAccountBalanceDetailComponent,
    EmployeeAccountBalanceUpdateComponent,
    EmployeeAccountBalanceDeleteDialogComponent,
  ],
})
export class EmployeeAccountBalanceModule {}
