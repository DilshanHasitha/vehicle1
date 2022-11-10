import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CashBookBalanceComponent } from './list/cash-book-balance.component';
import { CashBookBalanceDetailComponent } from './detail/cash-book-balance-detail.component';
import { CashBookBalanceUpdateComponent } from './update/cash-book-balance-update.component';
import { CashBookBalanceDeleteDialogComponent } from './delete/cash-book-balance-delete-dialog.component';
import { CashBookBalanceRoutingModule } from './route/cash-book-balance-routing.module';

@NgModule({
  imports: [SharedModule, CashBookBalanceRoutingModule],
  declarations: [
    CashBookBalanceComponent,
    CashBookBalanceDetailComponent,
    CashBookBalanceUpdateComponent,
    CashBookBalanceDeleteDialogComponent,
  ],
})
export class CashBookBalanceModule {}
