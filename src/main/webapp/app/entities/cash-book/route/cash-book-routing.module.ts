import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CashBookComponent } from '../list/cash-book.component';
import { CashBookDetailComponent } from '../detail/cash-book-detail.component';
import { CashBookUpdateComponent } from '../update/cash-book-update.component';
import { CashBookRoutingResolveService } from './cash-book-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cashBookRoute: Routes = [
  {
    path: '',
    component: CashBookComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CashBookDetailComponent,
    resolve: {
      cashBook: CashBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CashBookUpdateComponent,
    resolve: {
      cashBook: CashBookRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cashBookRoute)],
  exports: [RouterModule],
})
export class CashBookRoutingModule {}
