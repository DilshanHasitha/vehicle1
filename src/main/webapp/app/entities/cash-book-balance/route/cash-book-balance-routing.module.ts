import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CashBookBalanceComponent } from '../list/cash-book-balance.component';
import { CashBookBalanceDetailComponent } from '../detail/cash-book-balance-detail.component';
import { CashBookBalanceUpdateComponent } from '../update/cash-book-balance-update.component';
import { CashBookBalanceRoutingResolveService } from './cash-book-balance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cashBookBalanceRoute: Routes = [
  {
    path: '',
    component: CashBookBalanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CashBookBalanceDetailComponent,
    resolve: {
      cashBookBalance: CashBookBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CashBookBalanceUpdateComponent,
    resolve: {
      cashBookBalance: CashBookBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CashBookBalanceUpdateComponent,
    resolve: {
      cashBookBalance: CashBookBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cashBookBalanceRoute)],
  exports: [RouterModule],
})
export class CashBookBalanceRoutingModule {}
